// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.tomcat.extension.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.json.JSONParser;

/**
 * Logs setup information for the respective tribefire project, if a setup information file is {@link #setSetupInfoFile(String) specified} or exists
 * at its default location {@value #DEFAULT_SETUP_INFO_FILE}. The file must be a json file that represents a map. Its entries are logged in order, one
 * line for each pair.
 * <p>
 * This class is intentionally very simple, since it's Tomcat specific and thus shouldn't contain too much logic. It's basically just logging
 * key/value pairs.<br>
 * The content to be logged, i.e. what and which detail, must be provided by another (smarter) tool such as Jinni.
 */
public class SetupInfoLoggerListener implements LifecycleListener {

	private static final Log log = LogFactory.getLog(SetupInfoLoggerListener.class);

	/**
	 * The default path where the file is searched, if no other path is {@link #setSetupInfoFile(String) specified}.
	 */
	private static final String DEFAULT_SETUP_INFO_FILE = "../../../setup-info/setup-info.json";

	private String setupInfoFile;

	public String getSetupInfoFile() {
		return setupInfoFile;
	}

	public void setSetupInfoFile(String setupInfoFile) {
		this.setupInfoFile = setupInfoFile;
	}

	/**
	 * Runs {@link #log()} when receiving the initialization event.
	 */
	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType())) {
			log();
		}
	}

	/**
	 * Logs the setup information, if the respective file exists.
	 */
	private void log() {
		boolean setupInfoFileMustExist = this.setupInfoFile != null;
		File setupInfoFile = new File(setupInfoFileMustExist ? this.setupInfoFile : DEFAULT_SETUP_INFO_FILE);

		if (setupInfoFile.exists()) {
			Map<?, ?> setupInfo = parseSetupInfo(setupInfoFile);

			setupInfo.forEach((key, value) -> {
				// align with this Tomcat log statements
				int minimumKeyStringLength = "Command line argument: ".length();

				String keyString = key + ": ";
				if (keyString.length() < minimumKeyStringLength) {
					// add spaces
					keyString = String.format("%-" + minimumKeyStringLength + "s", keyString);
				}

				log.info(keyString + value);
			});
		} else {
			if (setupInfoFileMustExist) {
				throw new IllegalStateException("The configured setup info file " + setupInfoFile.getAbsolutePath() + " does not exist!");
			} else {
				// default does not exist at its default location.
				// this is fine, i.e. just don't log anything.
			}
		}
	}

	/**
	 * Parses the passed <code>setupInfoFile</code> and returns the result which is expected to be a map.
	 */
	private static LinkedHashMap<?, ?> parseSetupInfo(File setupInfoFile) {
		try {
			try (InputStream inputStream = new FileInputStream(setupInfoFile)) {
				JSONParser parser = new JSONParser(inputStream, "UTF-8");
				Object parsedObject = parser.parse();

				if (!(parsedObject instanceof Map)) {
					throw new IllegalStateException("Expected parsed object to be a map but found " + parsedObject.getClass().getName() + "!");
				}
				return (LinkedHashMap<?, ?>) parsedObject;
			}
		} catch (Exception e) {
			throw new IllegalStateException("Couldn't parse " + setupInfoFile + "!", e);
		}
	}
}
