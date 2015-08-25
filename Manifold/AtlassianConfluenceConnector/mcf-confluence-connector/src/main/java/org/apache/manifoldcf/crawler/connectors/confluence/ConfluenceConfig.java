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
package org.apache.manifoldcf.crawler.connectors.confluence;

/**
 * Zaizi (pvt) Ltd
 * 
 * @author kgunaratnam
 *
 */
public class ConfluenceConfig {
	public static final String CLIENT_ID_PARAM = "clientid";
	public static final String CLIENT_SECRET_PARAM = "clientsecret";
	public static final String CONF_PROTOCOL_PARAM = "confprotocol";
	public static final String CONF_HOST_PARAM = "confhost";
	public static final String CONF_PORT_PARAM = "confport";
	public static final String CONF_PATH_PARAM = "confpath";
	public static final String CONF_SOAP_API_PARAM = "confsoapapipath";

	public static final String CONF_PROXYHOST_PARAM = "confproxyhost";
	public static final String CONF_PROXYPORT_PARAM = "confproxyport";
	public static final String CONF_PROXYDOMAIN_PARAM = "confproxydomain";
	public static final String CONF_PROXYUSERNAME_PARAM = "confproxyusername";
	public static final String CONF_PROXYPASSWORD_PARAM = "confproxypassword";

	public static final String CONF_QUERY_PARAM = "confquery";

	public static final String CLIENT_ID_DEFAULT = "";
	public static final String CLIENT_SECRET_DEFAULT = "";
	public static final String CONF_PROTOCOL_DEFAULT = "http";
	public static final String CONF_HOST_DEFAULT = "";
	public static final String CONF_PORT_DEFAULT = "";
	public static final String CONF_PATH_DEFAULT = "/confluence/rest/api/content/";
	public static final String CONF_SOAP_API_PATH_DEFAULT = "/confluence/rpc/soap-axis/confluenceservice-v2";

	public static final String CONF_PROXYHOST_DEFAULT = "";
	public static final String CONF_PROXYPORT_DEFAULT = "";
	public static final String CONF_PROXYDOMAIN_DEFAULT = "";
	public static final String CONF_PROXYUSERNAME_DEFAULT = "";
	public static final String CONF_PROXYPASSWORD_DEFAULT = "";

	// will not be used for confluence //todo for future
	public static final String CONF_QUERY_DEFAULT = "";

	// Nodes
	public static final String JOB_STARTPOINT_NODE_TYPE = "startpoint";
	public static final String JOB_QUERY_ATTRIBUTE = "query";
	public static final String JOB_SECURITY_NODE_TYPE = "security";
	public static final String JOB_VALUE_ATTRIBUTE = "value";
	public static final String JOB_ACCESS_NODE_TYPE = "access";
	public static final String JOB_TOKEN_ATTRIBUTE = "token";

	// Configuration tabs
	public static final String CONF_SERVER_TAB_PROPERTY = "ConfRepositoryConnector.Server";
	public static final String CONF_PROXY_TAB_PROPERTY = "ConfRepositoryConnector.Proxy";

	// Specification tabs
	public static final String CONF_QUERY_TAB_PROPERTY = "ConfRepositoryConnector.Query";
	public static final String CONF_SECURITY_TAB_PROPERTY = "ConfRepositoryConnector.Security";

	// pages & js
	// Template names for configuration
	/**
	 * Forward to the javascript to check the configuration parameters
	 */
	public static final String EDIT_CONFIG_HEADER_FORWARD = "editConfiguration_conf.js";
	/**
	 * Server tab template
	 */
	public static final String EDIT_CONFIG_FORWARD_SERVER = "editConfiguration_conf_server.html";
	/**
	 * Proxy tab template
	 */
	public static final String EDIT_CONFIG_FORWARD_PROXY = "editConfiguration_conf_proxy.html";

	/**
	 * Forward to the HTML template to view the configuration parameters
	 */
	public static final String VIEW_CONFIG_FORWARD = "viewConfiguration_conf.html";

	// Template names for specification
	/**
	 * Forward to the javascript to check the specification parameters for the
	 * job
	 */
	public static final String EDIT_SPEC_HEADER_FORWARD = "editSpecification_conf.js";
	/**
	 * Forward to the template to edit the query for the job
	 */
	public static final String EDIT_SPEC_FORWARD_CONFQUERY = "editSpecification_confQuery.html";
	/**
	 * Forward to the template to edit the security parameters for the job
	 */
	public static final String EDIT_SPEC_FORWARD_SECURITY = "editSpecification_confSecurity.html";

	/**
	 * Forward to the template to view the specification parameters for the job
	 */
	public static final String VIEW_SPEC_FORWARD = "viewSpecification_conf.html";
	
}
