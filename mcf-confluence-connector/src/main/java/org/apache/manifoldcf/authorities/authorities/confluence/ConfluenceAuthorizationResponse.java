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

import org.apache.manifoldcf.core.interfaces.StringSet;

class ConfluenceAuthorizationResponse extends
		org.apache.manifoldcf.core.cachemanager.BaseDescription {

	protected String userName;

	protected String connectionString;

	/**
	 * The response lifetime
	 */
	protected long responseLifetime;

	/**
	 * The expiration time
	 */
	protected long expirationTime = -1;

	public ConfluenceAuthorizationResponse(String userName,
			String connectionString, long responseLifetime, int LRUsize) {
		super("ConfluenceAuthority", LRUsize);
		this.connectionString = connectionString;
		this.responseLifetime = responseLifetime;
		this.userName = userName;
	}

	@Override
	public String getCriticalSectionName() {
		StringBuilder sb = new StringBuilder(getClass().getName());
		sb.append("-").append(userName).append("-")
				.append(connectionString);
		return sb.toString();
	}

	@Override
	public StringSet getObjectKeys() {
		return ConfluenceAuthority.emptyStringSet;
	}

	@Override
	public long getObjectExpirationTime(long currentTime) {
		if (expirationTime == -1) {
			expirationTime = currentTime + responseLifetime;
		}
		return expirationTime;
	}

	@Override
	public int hashCode() {
		return userName.hashCode() + connectionString.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ConfluenceAuthorizationResponse)) {
			return false;
		}
		ConfluenceAuthorizationResponse ard = (ConfluenceAuthorizationResponse) o;
		if (!ard.userName.equals(userName)) {
			return false;
		}
		if (!ard.connectionString.equals(connectionString)) {
			return false;
		}
		return true;
	}

}