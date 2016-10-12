/*
 * Copyright (c) 2016 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.aexp.api.client.core.security.authentication;

import io.aexp.api.client.core.configuration.ConfigurationKeys;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * Provides an implementation of AuthProvider for the API Key only authentication schema.
 */
public class ApiKeyAuthProvider extends BaseAuthProvider {
	/**
	 * Generates the minimal headers required for authentication. The values for API Key header are derived by the ConfigurationProvider supplied.
	 * A GUID will be supplied for the request id header.
	 */
	@Override
	public Map<String, String> generateAuthHeaders(String reqPayload, String requestUrl, String httpMethod) {
		Map<String, String> headers = new Hashtable<String, String>();
		headers.put(AuthHeaderNames.X_AMEX_API_KEY, getConfigurationValue(ConfigurationKeys.CLIENT_KEY));
		headers.put(AuthHeaderNames.X_AMEX_REQUEST_ID, getRequestUUID());
		return Collections.unmodifiableMap(headers);
	}

}
