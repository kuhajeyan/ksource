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
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.manifoldcf.core.common.XThreadStringBuffer;
import org.apache.manifoldcf.crawler.system.Logging;

public abstract class ConfluenceContentProcessor {

	public Collection<Object> process(String rootResourceType, XThreadStringBuffer idBuffer, boolean shouldPush, boolean shouldProcessAttachment) {
		String urlQuery = rootResourceType+"/?";
		return processConfluence(urlQuery, idBuffer, shouldPush,
				shouldProcessAttachment);
	}
	
	public Collection<Object> process(String rootResourceType ,String contentType, String spaceKey, XThreadStringBuffer idBuffer, boolean shouldPush, boolean shouldProcessAttachment) {
		String urlQuery = String.format(rootResourceType+"?type=%s&spacKey=%s",contentType,spaceKey);
		return processConfluence(urlQuery, idBuffer, shouldPush,
				shouldProcessAttachment);
	}

	private Collection<Object> processConfluence( String urlPart, XThreadStringBuffer idBuffer, boolean shouldPush,
			boolean shouldProcessAttachment) {
		long startAt = 0L;
		long setSize = 500L;
		long totalAmt = 0L;
		List<Object> collection = new ArrayList<Object>();
		
		String nextLink = "";
		do {
			ConfluenceQueryResults qr = new ConfluenceQueryResults();
			//getRest("?type="+confluenceContentType+"&spaceKey=*&limit=" + setSize + "&start=" + startAt, qr);
			String urlQuery = String.format(urlPart+(!urlPart.endsWith("/?")?"&":"")+"limit=%d&start=%d", setSize, startAt);
			
			Collection<Object> doProcessedCollection = doProcess(qr, urlQuery);		
			if(doProcessedCollection != null)
				collection.addAll(doProcessedCollection);
			
			Long total = qr.getTotal();
			nextLink = qr.getNextLink();
			if (total == null)
				return null;
			totalAmt += total.longValue();
			try {
				if(shouldPush)
					qr.pushIds(idBuffer);				
				if(shouldProcessAttachment)
					doProcessAttachement(qr,idBuffer);
				
			} catch (IOException | InterruptedException e) {
				if(Logging.connectors != null)
					Logging.connectors.error(e);
			}
			
			
			startAt += total > 0 ? total : setSize;
			if (Logging.connectors != null)
				Logging.connectors.info("next link is : " + nextLink
						+ ", total amount :" + totalAmt
						+ ", start at next is : " + startAt);		
			
			
		}while (nextLink != null && StringUtils.isNotEmpty(nextLink));
		if (Logging.connectors != null)
			Logging.connectors
					.info("Seeding documents is finished total documents seeded : "
							+ totalAmt);
		
		return collection;
	}
	
	public abstract Collection<Object> doProcess(ConfluenceQueryResults qr, String urlQuery);
	
	public abstract void doProcessAttachement(ConfluenceQueryResults qr, XThreadStringBuffer idBuffer);
	
}
