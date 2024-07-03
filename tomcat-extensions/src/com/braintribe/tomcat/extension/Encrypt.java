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

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

/**
 * This class provides a main method for encrypting a password in the command line. <br>
 * The result is a string in the form <code>${AES/CBC/PKCS5Padding:keylength:....}</code>. <br>
 * Implementation note: this has been separated from the EncryptedPropertySource class so that it can be used independently of any other library.
 */
public class Encrypt {

	public static void main(String[] args) {

		if (args == null || args.length < 1) {
			System.err.println("Please provide a password that should be encrypted.");
			System.exit(1);
		}
		int keyLength = -1;
		if (args.length == 2) {
			keyLength = Integer.parseInt(args[1]);
		}
		if (keyLength <= 0) {
			keyLength = 256;
		}
		int maxAllowedKeyLength = 0;
		try {
			maxAllowedKeyLength = Cipher.getMaxAllowedKeyLength("AES");
		} catch (NoSuchAlgorithmException e1) {
			System.err.println("The required cipher AES is not available.");
			System.exit(2);
		}
		if (maxAllowedKeyLength > 0 && maxAllowedKeyLength < keyLength) {
			keyLength = maxAllowedKeyLength;
		}

		try {
			System.out.println("${" + EncryptedPropertySource.PREFIX + ":" + keyLength + ":" + Crypt.encrypt(args[0], keyLength) + "}");
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
}
