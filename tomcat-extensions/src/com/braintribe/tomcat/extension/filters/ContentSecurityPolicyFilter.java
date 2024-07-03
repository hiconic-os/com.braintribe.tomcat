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
package com.braintribe.tomcat.extension.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple filter which can be used to set Content Security Policy directives via HTTP header {@value #CONTENT_SECURITY_POLICY_HEADER_NAME}. The
 * directives to be set can be configured via filter init parameter {@value #DIRECTIVES_INIT_PARAMATER_NAME}.
 * <p>
 * For further information about cContent Security Policy see https://content-security-policy.com/.
 */
public class ContentSecurityPolicyFilter implements Filter {

	private static final String CONTENT_SECURITY_POLICY_HEADER_NAME = "Content-Security-Policy";
	private static final String DIRECTIVES_INIT_PARAMATER_NAME = "directives";

	private String directivesString;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader(CONTENT_SECURITY_POLICY_HEADER_NAME, directivesString);

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		directivesString = config.getInitParameter(DIRECTIVES_INIT_PARAMATER_NAME);
		// we could do some validation here
	}

	@Override
	public void destroy() {
		// nothing to do
	}
}
