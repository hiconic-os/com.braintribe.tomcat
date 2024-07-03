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

import static com.braintribe.testing.junit.assertions.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class EncryptedPropertySourceTest {

	@Test
	public void testSystemProps() throws Exception {

		EncryptedPropertySource instance = new EncryptedPropertySource();

		System.setProperty("hello", "world");

		String result = instance.getProperty("hello");

		assertThat(result).isNotNull();
		assertThat(result).isEqualTo("world");

	}

	@Test
	public void testDecryption() throws Exception {

		EncryptedPropertySource instance = new EncryptedPropertySource();

		String encrypted = "AES/CBC/PKCS5Padding:A2NLSWtB+se5MeD2m9qJ3c6UYbe8Dc9RRee02X2VUto69Ql2DmpWJXrBZZxvVf+x7HUjbw==";

		String result = instance.getProperty(encrypted);

		assertThat(result).isNotNull();
		assertThat(result).isEqualTo("cortex");

	}

	@Test
	public void testDecryptionWithKeyLength() throws Exception {

		EncryptedPropertySource instance = new EncryptedPropertySource();

		String encrypted = "AES/CBC/PKCS5Padding:128:5y+M4kFHswOJRQmzryLT3rYBLxGfRWATEPBLfNCSHFttW1PxB+GOZIdFBN9EK6Y5ReBnXQ==";

		String result = instance.getProperty(encrypted);

		assertThat(result).isNotNull();
		assertThat(result).isEqualTo("cortex");

	}

	@Test
	public void testDecryptionWithWrongKeyLength() throws Exception {

		EncryptedPropertySource instance = new EncryptedPropertySource();

		String encrypted = "AES/CBC/PKCS5Padding:128:A2NLSWtB+se5MeD2m9qJ3c6UYbe8Dc9RRee02X2VUto69Ql2DmpWJXrBZZxvVf+x7HUjbw=="; // Correct encoding,
																																// wrong keylength

		String result = instance.getProperty(encrypted);

		assertThat(result).isNull();

	}
}
