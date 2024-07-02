// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.tomcat.extension.filters;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple filter which adds fixed HTTP response headers. It can be used to easily inject headers for testing purposes, e.g. to test if an additional
 * header actually fixes the problem before implementing a proper solution.
 */
public class FixedHttpResponseHeadersFilter implements Filter {

	private Map<String, String> headers = new LinkedHashMap<>();

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;

		headers.forEach((name, value) -> response.setHeader(name, value));

		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		int i = 1;
		while (true) {
			String headerNameAndValue = config.getInitParameter("header-" + i);
			if (headerNameAndValue == null) {
				break;
			}

			String headerName;
			String headerValue = null;
			int equalsSignIndex = headerNameAndValue.indexOf(':');

			if (equalsSignIndex > 0) {
				headerName = headerNameAndValue.substring(0, equalsSignIndex).trim();
				headerValue = headerNameAndValue.substring(equalsSignIndex + 1).trim();
				headers.put(headerName, headerValue);
			} else {
				headerName = headerNameAndValue.trim();
			}

			headers.put(headerName, headerValue);

			i++;
		}
	}

	@Override
	public void destroy() {
		// nothing to do
	}
}
