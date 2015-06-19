
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
*/package org.apache.manifoldcf.confluence;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;
import org.apache.commons.lang.StringUtils;
import org.apache.manifoldcf.core.interfaces.ManifoldCFException;
import org.apache.manifoldcf.crawler.system.Logging;
import org.swift.common.soap.confluence.AuthenticationFailedException;
import org.swift.common.soap.confluence.ConfluenceSoapService;
import org.swift.common.soap.confluence.ConfluenceSoapServiceServiceLocator;
import org.swift.common.soap.confluence.InvalidSessionException;
import org.swift.common.soap.confluence.RemoteServerInfo;

public class ConfluenceSOAPServiceImpl implements ConfluenceService {

	private ConfluenceSoapServiceServiceLocator fConfluenceSoapServiceGetter = new ConfluenceSoapServiceServiceLocator();
	private ConfluenceSoapService fConfluenceSoapService = null;
	private String fToken = null;
	private int timeout = 10000;

	public ConfluenceSOAPServiceImpl(String protocol, String server, String port, String soapApiPath) throws ManifoldCFException {
		String endPoint = "/rpc/soap-axis/confluenceservice-v2";
		if (!StringUtils.isEmpty(soapApiPath)) {
			endPoint = soapApiPath;
		}
		String fullServiceUrl = String.format("%s://%s:%s", protocol, server,
				port) + endPoint;
		fConfluenceSoapServiceGetter
				.setConfluenceserviceV2EndpointAddress(fullServiceUrl);
		
		fConfluenceSoapServiceGetter.setMaintainSession(true);
		try {
			fConfluenceSoapService = fConfluenceSoapServiceGetter
					.getConfluenceserviceV2();
			
			//set timeout
			org.apache.axis.client.Stub s = (Stub) fConfluenceSoapService;
			s.setTimeout(timeout);
			
		} catch (ServiceException e) {
			Logging.connectors.error("Error while initializing connection", e);
			throw new ManifoldCFException(e.getCause());
 		}
	}

	@Override
	public String getfToken() {
		return fToken;
	}

	@Override
	public void setfToken(String fToken) {
		this.fToken = fToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.manifoldcf.confluence.ConfluenceService#getValidToken(java
	 * .lang.String, java.lang.String)
	 */

	@Override
	public String getValidToken(String userName, String password)
			throws ServiceException, RemoteException,
			AuthenticationFailedException,
			org.swift.common.soap.confluence.RemoteException {
		String token = null;
		if (fToken == null || StringUtils.isEmpty(fToken)
				|| !isValidToken(fToken)) {
			org.apache.axis.client.Stub s = (Stub) fConfluenceSoapService;
			s.setTimeout(timeout);
			fToken = fConfluenceSoapService.login(userName, password);
			token = fToken;
			if(Logging.connectors != null)
			Logging.connectors.info("Generate new token " + token);
		}

		return token;
	}

	@Override
	public boolean isValid() {
		return isValidToken(fToken);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.manifoldcf.confluence.ConfluenceService#isValidToken(java.
	 * lang.String)
	 */

	@Override
	public boolean isValidToken(String token) {
		if (!StringUtils.isEmpty(token)) {
			try {
				org.apache.axis.client.Stub s = (Stub) fConfluenceSoapService;
				s.setTimeout(timeout);
				RemoteServerInfo info = fConfluenceSoapService
						.getServerInfo(token);
				return info.getMajorVersion() > 0;
			} catch (InvalidSessionException e) {
				Logging.connectors.error("invalid session token", e);
			} catch (org.swift.common.soap.confluence.RemoteException e) {
				Logging.connectors.error("Remote exception", e);
			} catch (RemoteException e) {
				Logging.connectors.error("Remote exception", e);
			}
		}
		return false;
	}

	@Override
	public boolean close() throws ManifoldCFException {
		try {
			return fConfluenceSoapService.logout(fToken);
		} catch (RemoteException e) {
			Logging.connectors.error("Error while closing", e);
			throw new ManifoldCFException(e.getCause());
		}

		
	}

	@Override
	public ConfluenceSoapService getSoapService() throws ManifoldCFException {
		if (isValid())
			return fConfluenceSoapService;
		else
			throw new ManifoldCFException("Invalid service instance");
	}

}
