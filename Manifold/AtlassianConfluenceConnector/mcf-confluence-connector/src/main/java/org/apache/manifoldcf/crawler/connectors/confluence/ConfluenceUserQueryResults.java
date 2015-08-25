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

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Zaizi (pvt) Ltd
 * @author kgunaratnam
 *
 */
public class ConfluenceUserQueryResults extends ConfluenceJSONResponse{

	// Specific keys we care about
	private final static String KEY_NAME = "name";

	public ConfluenceUserQueryResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void getNames(List<String> nameBuffer) {
		JSONArray users = (JSONArray) object;
		for (Object user : users) {
			if (user instanceof JSONObject) {
				JSONObject jo = (JSONObject) user;
				nameBuffer.add(jo.get(KEY_NAME).toString());
			}
		}
	}

}
