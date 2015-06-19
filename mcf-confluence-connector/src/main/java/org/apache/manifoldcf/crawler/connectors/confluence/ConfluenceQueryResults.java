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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.manifoldcf.core.common.XThreadStringBuffer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Zaizi (pvt) Ltd
 * @author kgunaratnam
 *
 */
public class ConfluenceQueryResults extends ConfluenceJSONResponse {

	//Confluence fields from where we should pick the values from
	private final static String SIZE = "size";
	private final static String RESULTS = "results";
	private final static String ID = "id";
	private final static String LINKS = "_links";
	private List<String> ids;

	public ConfluenceQueryResults() {
		super();
		ids = new ArrayList<String>();
	}

	public Long getTotal() {
		if(object != null)
			return (Long) ((JSONObject) object).get(SIZE);
		return Long.valueOf(0);
	}

	public String getNextLink() {
		if( object != null){
			JSONObject j = (JSONObject) ((JSONObject) object).get(LINKS);
			if (j != null)
				return (String) j.get("next");
			else
				return null;
		}
		
		return null;
	}

	public void pushIds(XThreadStringBuffer seedBuffer) throws IOException,
			InterruptedException {
		
		//regular contents
		JSONArray contents = (JSONArray) ((JSONObject) object).get(RESULTS);
		for (Object content : contents) {
			if (content instanceof JSONObject) {
				JSONObject jo = (JSONObject) content;
				String id = jo.get(ID).toString();
				seedBuffer.add(id);
				ids.add(id);
			}
		}
	}
	
	public Object getResults(){
		return ((JSONObject) object).get(RESULTS);
	}

	public List<String> getIds() {
		return ids;
	}
	
	

}
