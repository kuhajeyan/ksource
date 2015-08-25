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

import org.apache.manifoldcf.confluence.ConfluenceService;
import org.apache.manifoldcf.crawler.connectors.confluence.ResponseException;

public class CheckConnectionThread extends Thread {

	protected final ConfluenceService service;
	protected Throwable exception = null;
	protected String result = "Unknown";

	public CheckConnectionThread(ConfluenceService session) {
		super();
		this.service = session;
		setDaemon(true);
	}

	public Throwable getException() {
		return exception;
	}

	public void run() {
		try {
			if (service.isValid())
				result = "Connection OK";
			else {
				result = "Connection Failure";
			}
		} catch (Throwable e) {
			this.exception = e;
		}
	}
	
	public String getResult(){
		return result;
	}

	public void finishUp() throws InterruptedException, IOException,
			ResponseException {
		join();
		Throwable thr = exception;
		if (thr != null) {
			if (thr instanceof IOException) {
				throw (IOException) thr;
			} else if (thr instanceof ResponseException) {
				throw (ResponseException) thr;
			} else if (thr instanceof RuntimeException) {
				throw (RuntimeException) thr;
			} else {
				throw (Error) thr;
			}
		}
	}
}
