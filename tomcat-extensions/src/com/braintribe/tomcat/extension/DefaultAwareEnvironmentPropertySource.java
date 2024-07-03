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
package com.braintribe.tomcat.extension;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tomcat.util.IntrospectionUtils.PropertySource;

/**
 */
public class DefaultAwareEnvironmentPropertySource implements PropertySource {

	private static final Logger logger = Logger.getLogger(DefaultAwareEnvironmentPropertySource.class.getName());

	@Override
	public String getProperty(String key) {

		logger.fine("getProperty called for key " + key);

		if (key != null && key.startsWith("?")) {

			logger.fine("Key seems to be an encoded specification");

			Map<String, String> spec = splitSpecification(key);

			logger.fine("Read from " + key + ": " + spec);

			String envName = spec.get("envName");
			if (envName != null) {
				String value = getEnvironmentVariable(envName);
				if (value != null) {
					return value;
				} else {
					String defaultValue = spec.get("default");
					return defaultValue;
				}
			}
		}
		return null;
	}

	public Map<String, String> splitSpecification(String input) {
		// Get rid of the leading ?
		input = input.substring(1);
		String[] split = input.split("&");
		Map<String, String> result = new LinkedHashMap<>();
		for (String entry : split) {
			final int idx = entry.indexOf("=");
			if (idx > 0 && idx < entry.length() - 1) {
				String key = entry.substring(0, idx).trim();
				String value = entry.substring(idx + 1).trim();

				try {
					result.put(URLDecoder.decode(key, "UTF-8"), URLDecoder.decode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.log(Level.SEVERE, "Error while trying to parse " + entry, e);
				}
			}

		}
		return result;
	}

	private String getEnvironmentVariable(String name) {
		Map<String, String> getenv = System.getenv();
		String value;

		value = getenv.get(name);
		if (value != null) {
			return value;
		}

		value = System.getProperty(name);
		return value;
	}
}
