
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
*/package org.apache.manifoldcf.crawler.connectors.confluence;

import org.apache.manifoldcf.core.common.DateParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Zaizi (pvt) Ltd
 * @author kgunaratnam
 *
 */
public class ConfluenceContent extends ConfluenceJSONResponse {


    private final static String KEY_LINKS = "_links";
	private final static String KEY_ID = "id";
	private final static String KEY_SELF = "self";
	private final static String KEY_WEBUI = "webui";
	private final static String KEY_DOWNLOAD = "download";
	private final static String KEY_BASE = "base";
	private final static String KEY_NAME = "name";
	private final static String KEY_KEY = "key";
	private final static String KEY_TITLE = "title";
	private final static String KEY_LABELS = "labels";
	private final static String KEY_BODY = "body";
	private final static String KEY_VIEW = "view";
	private final static String KEY_RAW_CONTENT = "content_raw";
	private final static String KEY_METADATA = "metadata";
	private final static String KEY_SPACE = "space";
	private final static String KEY_HISTORY = "history";
	private final static String KEY_CREATED_DATE = "createdDate";
	private final static String KEY_CREATED_BY = "createdBy";
	private final static String KEY_BY = "by";
	private final static String KEY_TYPE = "type";
	private final static String KEY_DISPLAY_NAME = "displayName";
	private final static String KEY_USER_NAME = "username";
	private final static String KEY_VERSION = "version";
	private final static String KEY_NUMBER = "number";
	private static final String KEY_LANG = "language";
	private static final String KEY_AUTHOR = "author";
	private static final String KEY_CREATOR = "creator";
	private static final String KEY_LAST_MODIFIED_DATE = "lastModifiedDate";
	private static final String KEY_LAST_MODIFIER = "lastModifier";
	private static final String KEY_MIME_TYPE = "mimeType";
	private static final String KEY_SIZE = "size";
	private static final String KEY_WHEN = "when";
    public static final String SOLR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public ConfluenceContent() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public String getType() {
		Object key = ((JSONObject) object).get(KEY_TYPE);
		if (key == null)
			return null;
		return key.toString();
	}

	public String getSpaceKey() {
		JSONObject space = (JSONObject) ((JSONObject) object).get(KEY_SPACE);
		Object obj = space.get(KEY_KEY);
		if (obj != null) {
			return obj.toString();
		}

		return null;
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
	
	public String getWebURL() {
		JSONObject links = (JSONObject) ((JSONObject) object).get(KEY_LINKS);
		if(links == null)
			return null;
		
		String webUrl = (String) links.get(KEY_WEBUI);
		String base = (String) links.get(KEY_BASE);
		
		if(webUrl == null || base ==null){
			return null;
		}
		return base + webUrl;
	}
	
	public String getDownLoadURL() {
		JSONObject links = (JSONObject) ((JSONObject) object).get(KEY_LINKS);
		if(links == null)
			return null;
		
		String downloadURL = (String) links.get(KEY_DOWNLOAD);
		String base = (String) links.get(KEY_BASE);
		
		if(downloadURL == null || base ==null){
			return null;
		}
		return base + downloadURL;
	}

	public String getContent() {
		JSONObject body = (JSONObject) ((JSONObject) object).get(KEY_BODY);
		if (body == null)
			return null;

		JSONObject view = (JSONObject) body.get(KEY_VIEW);
		if (view == null)
			return null;

		Object obj = view.get("value");
		if (obj == null)
			return null;

		return obj.toString();
	}

	public Date getCreatedDate() {
		JSONObject history = (JSONObject) ((JSONObject) object)
				.get(KEY_HISTORY);
		if (history == null)
			return null;

		Object createdDate = history.get(KEY_CREATED_DATE);
		if (createdDate == null)
			return null;
		
		return DateParser.parseISO8601Date(createdDate.toString());

	}

	public String getCreatedByDisplayName() {
		JSONObject history = (JSONObject) ((JSONObject) object)
				.get(KEY_HISTORY);
		if (history == null)
			return null;

		JSONObject createdBy = (JSONObject)  history.get(KEY_CREATED_BY);
		if (createdBy == null)
			return null;

		Object obj = createdBy.get(KEY_DISPLAY_NAME);
		if (obj == null)
			return null;

		return obj.toString();
	}
	
	public String getCreatedByUserName() {
		JSONObject history = (JSONObject) ((JSONObject) object)
				.get(KEY_HISTORY);
		if (history == null)
			return null;

		JSONObject createdBy = (JSONObject) history.get(KEY_CREATED_BY);
		if (createdBy == null)
			return null;

		Object obj = createdBy.get(KEY_USER_NAME);
		if (obj == null)
			return null;

		return obj.toString();
	}
	
	public String getLastModifierByUserName() {
		JSONObject version = (JSONObject) ((JSONObject) object)
				.get(KEY_VERSION);
		if (version == null)
			return null;

		JSONObject by = (JSONObject) version.get(KEY_BY);
		if (by == null)
			return null;

		Object obj = by.get(KEY_USER_NAME);
		if (obj == null)
			return null;

		return obj.toString();
	}
	
	public Date getLastModified() {
		JSONObject version = (JSONObject) ((JSONObject) object)
				.get(KEY_VERSION);
		if (version == null)
			return null;

		
		String when =  (String) version.get(KEY_WHEN);
		
		if(when != null){
			return DateParser.parseISO8601Date(when);
		}
		
		return null;
	}
	
	public Map<String,String[]> getMetadata() {	  
				
		Map<String,String[]> rmap = new HashMap<String,String[]>();
		
		//name
		rmap.put(KEY_NAME, new String[]{getTitle()});
		//title
		rmap.put(KEY_TITLE, new String[]{getTitle()});
		//author
		rmap.put(KEY_AUTHOR, new String[]{getCreatedByUserName()});
		
		//createdDate
		DateFormat formatter = new SimpleDateFormat(SOLR_DATE_FORMAT);
		String dateOut = formatter.format(getCreatedDate());		
		rmap.put(KEY_CREATED_DATE, new String[]{dateOut});
		
		String modifiedDate = formatter.format(getLastModified());
		rmap.put(KEY_LAST_MODIFIED_DATE, new String[]{modifiedDate});
		
		//creator
		rmap.put(KEY_CREATOR, new String[]{getCreatedByUserName()});//not available assigned default
		//lastModifier
		rmap.put(KEY_LAST_MODIFIER, new String[]{getLastModifierByUserName()});//not available assigned default
		//language
		rmap.put(KEY_LANG, new String[]{"en_US"});//not available assigned default
		//mimeType
		rmap.put(KEY_MIME_TYPE, new String[]{"text/html"});//not available assigned default
		//size
		//rmap.put(KEY_SIZE, new String[]{"0"});//not available assigned default
		
		
		//manually add the other meta fields here
		rmap.put(KEY_ID, new String[]{getID()});
		rmap.put(KEY_USER_NAME, new String[]{getCreatedByUserName()});
		rmap.put(KEY_DISPLAY_NAME, new String[]{getCreatedByDisplayName()});		
		rmap.put(KEY_SELF, new String[]{getURL()});
		rmap.put(KEY_WEBUI, new String[]{getWebURL()});
		rmap.put(KEY_SPACE, new String[]{getSpaceKey()});
		rmap.put(KEY_TYPE, new String[]{getType()});
		
		
		
		
		return rmap;
	  }

	  protected static void addMetadataToMap(String parent, Object cval, Map<String,List<String>> currentMap) {

	    if (cval == null)
	      return;

	    // See if it is a basic type
	    if (cval instanceof String || cval instanceof Number || cval instanceof Boolean) {
	      List<String> current = currentMap.get(parent);
	      if (current == null) {
	        current = new ArrayList<String>();
	        currentMap.put(parent,current);
	      }
	      current.add(cval.toString());
	      return;
	    }

	    // See if it is an array
	    if (cval instanceof JSONArray) {
	      JSONArray ja = (JSONArray)cval;
	      for (Object subpiece : ja) {
	        addMetadataToMap(parent, subpiece, currentMap);
	      }
	      return;
	    }
	    
	    // See if it is a JSONObject
	    if (cval instanceof JSONObject) {
	      JSONObject jo = (JSONObject)cval;
	      String append="";
	      if (parent.length() > 0) {
	        append=parent+"_";
	      }
	      for (Object key : jo.keySet()) {
	        Object value = jo.get(key);
	        if (value == null) {
	          continue;
	        }
	        String newKey = append + key;
	        addMetadataToMap(newKey, value, currentMap);
	      }
	      return;
	    }
	    

	    throw new IllegalArgumentException("Unknown object to addMetadataToMap: "+cval.getClass().getName());
	  }
}
