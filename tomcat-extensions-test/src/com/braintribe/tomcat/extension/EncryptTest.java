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
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class EncryptTest {

	@Test
	public void testEncryption() throws Exception {

		String result = null;
		PrintStream old = System.out;
		try {
			// Create a stream to hold the output
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			System.setOut(ps);

			Encrypt.main(new String[] { "cortex" });

			// Put things back
			System.out.flush();

			result = baos.toString().trim();

		} finally {
			System.setOut(old);
		}

		assertThat(result).isNotNull();
		assertThat(result).startsWith("${" + EncryptedPropertySource.PREFIX + ":");
		assertThat(result).endsWith("}");
		assertThat(result.length()).isGreaterThan(EncryptedPropertySource.PREFIX.length() + 4);

	}

}
