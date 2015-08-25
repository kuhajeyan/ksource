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

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class ConfluenceAttachment extends ConfluenceJSONResponse {

	public ConfluenceAttachment() {
		super();
		// TODO Auto-generated constructor stub
	}

	private final static String KEY_ID = "id";
	
	private final static String KEY_STATUS = "status";
	private final static String KEY_TITLE = "title";
	private final static String KEY_LINKS = "_links";
	private final static String KEY_METADATA = "metadata";
	private final static String KEY_SELF = "self";
	private final static String KEY_DOWNLOAD = "download";
	private final static String KEY_MEDIATYPE = "mediaType";	
	private final static String KEY_COMMENT = "mediaType";
	
	public String getID() {
		Object key = ((JSONObject) object).get(KEY_ID);
		if (key == null)
			return null;
		return key.toString();
	}

	public String getTitle() {
		Object key = ((JSONObject) object).get(KEY_TITLE);
		if (key == null)
			return null;
		return key.toString();
	}

	
	
	public String getStatus() {
		Object key = ((JSONObject) object).get(KEY_STATUS);
		if (key == null)
			return null;
		return key.toString();
	}
	
	public String getURL() {
		JSONObject links = (JSONObject) ((JSONObject) object).get(KEY_LINKS);
		if(links == null)
			return null;
		
		Object selfUrl = links.get(KEY_SELF);
		if(selfUrl == null)
			return null;
		
		return selfUrl.toString();
	}
	
	public String getDownloadURL() {
		JSONObject links = (JSONObject) ((JSONObject) object).get(KEY_LINKS);
		if(links == null)
			return null;
		
		Object selfUrl = links.get(KEY_DOWNLOAD);
		if(selfUrl == null)
			return null;
		
		return selfUrl.toString();
	}
	
	public Map<String,String[]> getMetadata() {	  
		
		Map<String,String[]> rmap = new HashMap<String,String[]>();
			
		//label
		JSONObject metaData = (JSONObject) ((JSONObject) object).get(KEY_METADATA);
		if(metaData != null)
		{
			extractInnerFieldValue(rmap, metaData,KEY_MEDIATYPE);
			extractInnerFieldValue(rmap, metaData,KEY_COMMENT);
		}
		
		return rmap;
	}

	private void extractInnerFieldValue(Map<String, String[]> rmap,
			JSONObject metaData, String keyField) {		
		if(metaData.get(keyField) != null){
			String[] labels = new String[] {metaData.get(keyField).toString()};
			rmap.put(keyField, labels);
		}
		else{
			rmap.put(keyField, new String[]{""});
		}
	}
	

}
