/**
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.manifoldcf.authorities.authorities.confluence;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.manifoldcf.agents.interfaces.ServiceInterruption;
import org.apache.manifoldcf.authorities.authorities.BaseAuthorityConnector;
import org.apache.manifoldcf.authorities.interfaces.AuthorizationResponse;
import org.apache.manifoldcf.confluence.ConfluenceSOAPServiceImpl;
import org.apache.manifoldcf.confluence.ConfluenceService;
import org.apache.manifoldcf.core.interfaces.CacheManagerFactory;
import org.apache.manifoldcf.core.interfaces.ConfigParams;
import org.apache.manifoldcf.core.interfaces.ICacheCreateHandle;
import org.apache.manifoldcf.core.interfaces.ICacheDescription;
import org.apache.manifoldcf.core.interfaces.ICacheHandle;
import org.apache.manifoldcf.core.interfaces.ICacheManager;
import org.apache.manifoldcf.core.interfaces.IHTTPOutput;
import org.apache.manifoldcf.core.interfaces.IPasswordMapperActivity;
import org.apache.manifoldcf.core.interfaces.IPostParameters;
import org.apache.manifoldcf.core.interfaces.IThreadContext;
import org.apache.manifoldcf.core.interfaces.ManifoldCFException;
import org.apache.manifoldcf.core.interfaces.StringSet;
import org.apache.manifoldcf.core.util.URLEncoder;
import org.apache.manifoldcf.crawler.connectors.confluence.ConfluenceConfig;
import org.apache.manifoldcf.crawler.connectors.confluence.ResponseException;
import org.apache.manifoldcf.crawler.system.Logging;
import org.swift.common.soap.confluence.ConfluenceSoapService;
import org.swift.common.soap.confluence.ConfluenceSoapServiceServiceLocator;
import org.swift.common.soap.confluence.RemoteSpaceSummary;

/**
 * Zaizi (pvt) Ltd
 * 
 * @author kgunaratnam
 *
 */
public class ConfluenceAuthority extends BaseAuthorityConnector {

	private static final String globalDenyToken = "DEAD_AUTHORITY";

	private static final AuthorizationResponse unreachableResponse = new AuthorizationResponse(
			new String[] { globalDenyToken },
			AuthorizationResponse.RESPONSE_UNREACHABLE);

	private static final AuthorizationResponse userNotFoundResponse = new AuthorizationResponse(
			new String[] { globalDenyToken },
			AuthorizationResponse.RESPONSE_USERNOTFOUND);

	private int connectionTimeoutMillis = 60 * 1000;

	private int socketTimeoutMillis = 30 * 60 * 1000;

	private long responseLifetime = 60000L; // 60sec

	private static final long timeToRelease = 300000L;

	private int LRUsize = 1000;

	private long sessionExpirationTime = -1L;
	protected long lastSessionFetch = -1L;

	// Parameter data
	protected String confprotocol = null;
	protected String confhost = null;
	protected String confport = null;
	protected String confsoapapipath = null;
	protected String clientid = null;
	protected String clientsecret = null;

	protected ConfluenceService service = null;

	/**
	 * Cache manager.
	 */
	private ICacheManager cacheManager = null;

	public ConfluenceAuthority() {

	}

	/**
	 * Set thread context.
	 */
	@Override
	public void setThreadContext(IThreadContext tc) throws ManifoldCFException {
		super.setThreadContext(tc);
		cacheManager = CacheManagerFactory.make(tc);
	}

	@Override
	public void connect(ConfigParams configParams) {
		super.connect(configParams);

		confprotocol = params
				.getParameter(ConfluenceAuthorityConfig.CONF_PROTOCOL_PARAM);
		confhost = params.getParameter(ConfluenceAuthorityConfig.CONF_HOST_PARAM);
		confport = params.getParameter(ConfluenceAuthorityConfig.CONF_PORT_PARAM);
		confsoapapipath = params.getParameter(ConfluenceAuthorityConfig.CONF_SOAP_API_PARAM);
		clientid = params.getParameter(ConfluenceAuthorityConfig.CLIENT_ID_PARAM);
		clientsecret = params
				.getObfuscatedParameter(ConfluenceAuthorityConfig.CLIENT_SECRET_PARAM);

		try {
			getConfluenceService();
		} catch (ManifoldCFException e) {
			Logging.connectors.error(e);
		}

	}

	private void getConfluenceService() throws ManifoldCFException {

		try {
			service = new ConfluenceSOAPServiceImpl(confprotocol, confhost,	confport, confsoapapipath);
			service.getValidToken(clientid, clientsecret);
		} catch (RemoteException | ServiceException | ManifoldCFException e) {
			Logging.connectors.error("Error while connecting", e);
			throw new ManifoldCFException(e.getCause());
		}
	}

	@Override
	public void poll() throws ManifoldCFException {
		if (lastSessionFetch == -1L) {
			return;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime >= lastSessionFetch + timeToRelease) {
			service.close();
			service = null;
			lastSessionFetch = -1L;
		}

		super.poll();
	}

	@Override
	public boolean isConnected() {
		return service.isValid();
	}

	@Override
	public String check() throws ManifoldCFException {
		try {
			return checkConnection();			
		} catch (ServiceInterruption e) {
			Logging.connectors.error("Error while checking connection", e);
			throw new ManifoldCFException(e.getCause());
		}
		
	}

	@Override
	public void disconnect() throws ManifoldCFException {
		service.close();
		super.disconnect();
	}

	protected String createCacheConnectionString() {
		StringBuilder sb = new StringBuilder();
		sb.append(confhost).append("#").append(clientid);
		return sb.toString();
	}

	@Override
	public AuthorizationResponse getAuthorizationResponse(String userName)
			throws ManifoldCFException {

		ICacheDescription objectDescription = new ConfluenceAuthorizationResponse(
				userName, createCacheConnectionString(), this.responseLifetime,
				this.LRUsize);

		// Enter the cache
		ICacheHandle ch = cacheManager.enterCache(
				new ICacheDescription[] { objectDescription }, null, null);
		try {
			ICacheCreateHandle createHandle = cacheManager
					.enterCreateSection(ch);
			try {
				// Lookup the object
				AuthorizationResponse response = (AuthorizationResponse) cacheManager
						.lookupObject(createHandle, objectDescription);
				if (response != null) {
					return response;
				}
				// Create the object.
				response = getAuthorizationResponseUncached(service, userName);
				// Save it in the cache
				cacheManager.saveObject(createHandle, objectDescription,
						response);
				// And return it...
				return response;
			} finally {
				cacheManager.leaveCreateSection(createHandle);
			}
		} finally {
			cacheManager.leaveCache(ch);
		}

	}

	private AuthorizationResponse getAuthorizationResponseUncached(
			ConfluenceService service, String userName) {
		ArrayList<String> tokens = new ArrayList<String>();
		
		
		//get all space keys
		try {
			ConfluenceSoapService soapService = service.getSoapService();
			
			
			RemoteSpaceSummary[] spaces = soapService.getSpaces(service.getfToken());
			for (RemoteSpaceSummary remoteSpaceSummary : spaces) {
				StringBuilder sb = new StringBuilder();
				sb.append(remoteSpaceSummary.getKey()).append("-");
				String[] permissionsForSpace;
				try {
					permissionsForSpace = soapService.getPermissionsForUser(service.getfToken(),remoteSpaceSummary.getKey(), userName);
					int counter = 0;
 					for (String permission : permissionsForSpace) {
 						if(counter == 0)
 							sb.append(permission);
 						else
 							sb.append("-").append(permission);
 						counter++;
					}
					tokens.add(sb.toString());
				} catch (Exception e) {
					Logging.connectors.error("Error while getting permission",e);					
				}			
				
			}
			
			
			
		} catch (ManifoldCFException | RemoteException e) {
			Logging.connectors.error("Error while getting permissions",e);
		}
		
		return new AuthorizationResponse(tokens.toArray(new String[tokens.size()]), AuthorizationResponse.RESPONSE_OK);
	}

	@Override
	public AuthorizationResponse getDefaultAuthorizationResponse(String userName) {
		return unreachableResponse;
	}
	

	@Override
	public void outputConfigurationHeader(IThreadContext threadContext,
			IHTTPOutput out, Locale locale, ConfigParams parameters,
			List<String> tabsArray) throws ManifoldCFException, IOException {
		//add only one header
		String tabName = Messages.getString(locale, "ConfAuthorityConnector.Server");
		tabsArray.add(tabName);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		//fill server
		fillInServerConfigurationMap(paramMap, out, parameters);
		
		//add javascript validations
		Messages.outputResourceWithVelocity(out, locale,
                "editAuthorityConfiguration.js", paramMap);
	}

	private void fillInServerConfigurationMap(Map<String, Object> newMap,
			IPasswordMapperActivity mapper, ConfigParams parameters) {
		
		String confprotocol = parameters.getParameter(ConfluenceAuthorityConfig.CONF_PROTOCOL_PARAM);
		String confhost = parameters.getParameter(ConfluenceAuthorityConfig.CONF_HOST_PARAM);
		String confport = parameters.getParameter(ConfluenceAuthorityConfig.CONF_PORT_PARAM);
		String confsoapapipath = parameters.getParameter(ConfluenceAuthorityConfig.CONF_SOAP_API_PARAM);
		
		String clientid = parameters.getParameter(ConfluenceAuthorityConfig.CLIENT_ID_PARAM);
		String clientsecret = parameters.getObfuscatedParameter(ConfluenceAuthorityConfig.CLIENT_SECRET_PARAM);

		if (confprotocol == null)
			confprotocol = ConfluenceAuthorityConfig.CONF_PROTOCOL_DEFAULT;
		if (confhost == null)
			confhost = ConfluenceAuthorityConfig.CONF_HOST_DEFAULT;
		if (confport == null)
			confport = ConfluenceAuthorityConfig.CONF_PORT_DEFAULT;
		if (confsoapapipath == null)
			confsoapapipath = ConfluenceAuthorityConfig.CONF_SOAP_API_PATH_DEFAULT;
		

		if (clientid == null)
			clientid = ConfluenceConfig.CLIENT_ID_DEFAULT;
		if (clientsecret == null)
			clientsecret = ConfluenceConfig.CLIENT_SECRET_DEFAULT;
		else
			clientsecret = mapper.mapPasswordToKey(clientsecret);

		newMap.put("CONFPROTOCOL", confprotocol);
		newMap.put("CONFHOST", confhost);
		newMap.put("CONFPORT", confport);		
		newMap.put("CLIENTID", clientid);
		newMap.put("CLIENTSECRET", clientsecret);
		newMap.put("CONF_SOAP_API_PATH", confsoapapipath);
	}

	@Override
	public void outputConfigurationBody(IThreadContext threadContext,
			IHTTPOutput out, Locale locale, ConfigParams parameters,
			String tabName) throws ManifoldCFException, IOException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// Set the tab name
		paramMap.put("TabName", tabName);
		fillInServerConfigurationMap(paramMap, out, parameters);
		Messages.outputResourceWithVelocity(out, locale, "editAuthorityConfiguration_server.html", paramMap);
	}

	@Override
	public String processConfigurationPost(IThreadContext threadContext,
			IPostParameters variableContext, Locale locale,
			ConfigParams parameters) throws ManifoldCFException {
		String confprotocol = variableContext.getParameter(ConfluenceAuthorityConfig.CONF_PROTOCOL_PARAM);
		if (confprotocol != null)
			parameters.setParameter(ConfluenceAuthorityConfig.CONF_PROTOCOL_PARAM,
					confprotocol);

		String confhost = variableContext.getParameter(ConfluenceAuthorityConfig.CONF_HOST_PARAM);
		if (confhost != null)
			parameters.setParameter(ConfluenceAuthorityConfig.CONF_HOST_PARAM, confhost);

		String confport = variableContext.getParameter(ConfluenceAuthorityConfig.CONF_PORT_PARAM);
		if (confport != null)
			parameters.setParameter(ConfluenceAuthorityConfig.CONF_PORT_PARAM, confport);
		
		String confsoapapipath = variableContext.getParameter(ConfluenceAuthorityConfig.CONF_SOAP_API_PARAM);
		if (confsoapapipath != null)
			parameters.setParameter(ConfluenceAuthorityConfig.CONF_SOAP_API_PARAM, confsoapapipath);
		
		String clientid = variableContext.getParameter(ConfluenceAuthorityConfig.CLIENT_ID_PARAM);
		if (clientid != null)
			parameters.setParameter(ConfluenceAuthorityConfig.CLIENT_ID_PARAM, clientid);

		String clientsecret = variableContext.getParameter(ConfluenceAuthorityConfig.CLIENT_SECRET_PARAM);
		if (clientsecret != null)
			parameters.setObfuscatedParameter(
					ConfluenceAuthorityConfig.CLIENT_SECRET_PARAM,
					variableContext.mapKeyToPassword(clientsecret));
		
		//should return null 
		return null;
	}

	@Override
	public void viewConfiguration(IThreadContext threadContext,
			IHTTPOutput out, Locale locale, ConfigParams parameters)
			throws ManifoldCFException, IOException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		fillInServerConfigurationMap(paramMap, out, parameters);
		Messages.outputResourceWithVelocity(out, locale, "viewAuthorityConfiguration.html",
                paramMap);
	}

	protected String checkConnection() throws ManifoldCFException,
			ServiceInterruption {
		String result = null;
		CheckConnectionThread t = new CheckConnectionThread(service);
		try {
			t.start();
			t.finishUp();
			if (t.getException() != null) {
		        Throwable thr = t.getException();
		        return "Check exception: " + thr.getMessage();
		      }
			result = t.getResult();
		} catch (InterruptedException e) {
			t.interrupt();
			throw new ManifoldCFException("Interrupted: " + e.getMessage(), e,
					ManifoldCFException.INTERRUPTED);
		} catch (java.net.SocketTimeoutException e) {
			handleIOException(e);
		} catch (InterruptedIOException e) {
			t.interrupt();
			handleIOException(e);
		} catch (IOException e) {
			handleIOException(e);
		} catch (ResponseException e) {
			handleResponseException(e);
		}
		
		return result;
	}

	private static void handleIOException(IOException e)
			throws ManifoldCFException, ServiceInterruption {
		if (!(e instanceof java.net.SocketTimeoutException)
				&& (e instanceof InterruptedIOException)) {
			throw new ManifoldCFException("Interrupted: " + e.getMessage(), e,
					ManifoldCFException.INTERRUPTED);
		}
		Logging.connectors.warn(" IO exception: " + e.getMessage(), e);
		long currentTime = System.currentTimeMillis();
		throw new ServiceInterruption("IO exception: " + e.getMessage(), e,
				currentTime + 300000L, currentTime + 3 * 60 * 60000L, -1, false);
	}

	private static void handleResponseException(ResponseException e)
			throws ManifoldCFException, ServiceInterruption {
		Logging.connectors.error("Response exception " + e.getMessage());
		throw new ManifoldCFException("Unexpected response: " + e.getMessage(),
				e);
	}

	protected static StringSet emptyStringSet = new StringSet();

	

}
