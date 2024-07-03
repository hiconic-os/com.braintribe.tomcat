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
import static org.junit.Assert.fail;

import org.junit.Test;

public class CryptTest {

	@Test
	public void testEncryptionAndDecryption() throws Exception {

		String pw = "hello, world";

		String encrypted = Crypt.encrypt(pw, 128);

		assertThat(encrypted).isNotNull();
		assertThat(encrypted.length()).isGreaterThan(0);
		assertThat(encrypted).isNotEqualTo(pw);
		assertThat(encrypted).doesNotStartWith("${" + EncryptedPropertySource.PREFIX + ":");

		String decrypted = Crypt.decrypt(encrypted, 128);

		assertThat(decrypted).isNotNull();
		assertThat(decrypted.length()).isGreaterThan(0);
		assertThat(decrypted).isEqualTo(pw);

	}

	@Test
	public void testSalting() throws Exception {

		String pw = "hello, world";

		String encrypted1 = Crypt.encrypt(pw, 128);

		assertThat(encrypted1).isNotNull();
		assertThat(encrypted1.length()).isGreaterThan(0);

		String encrypted2 = Crypt.encrypt(pw, 128);

		assertThat(encrypted2).isNotNull();
		assertThat(encrypted2.length()).isGreaterThan(0);

		assertThat(encrypted1).isNotEqualTo(encrypted2);

	}

	@Test
	public void testDecryptionWithWrongKeyLength() throws Exception {
		// Correct encoding, wrong keylength
		String encrypted = "A2NLSWtB+se5MeD2m9qJ3c6UYbe8Dc9RRee02X2VUto69Ql2DmpWJXrBZZxvVf+x7HUjbw==";
		try {
			Crypt.decrypt(encrypted, 128);
			fail("This should have thrown an error.");
		} catch (Exception expected) {
			// Do nothing
		}
	}
}
