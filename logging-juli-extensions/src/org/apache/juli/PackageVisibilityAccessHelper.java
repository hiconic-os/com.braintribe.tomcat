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
package org.apache.juli;

import java.util.logging.Logger;

import org.apache.juli.ClassLoaderLogManager.ClassLoaderLogInfo;
import org.apache.juli.ClassLoaderLogManager.LogNode;

/**
 * Simple helper class used to access package-visible members of classes in the <code>org.apache.juli</code> package.
 *
 * @author michael.lafite
 */
public class PackageVisibilityAccessHelper {

	public static ClassLoaderLogInfo invokeClassLoaderLogInfoConstructor(final LogNode rootNode) {
		return new ClassLoaderLogInfo(rootNode);
	}

	public static LogNode invokeLogNodeConstructor(final LogNode parent, final Logger logger) {
		return new LogNode(parent, logger);
	}
}
