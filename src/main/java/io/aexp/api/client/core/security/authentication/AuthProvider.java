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

import io.aexp.api.client.core.configuration.ConfigurationProvider;

import java.util.Map;


/**
 * Defines the interface for authentication providers.
 */
public interface AuthProvider {

	/**
	 * Set the configuration provider to be used in the generation of the authentication headers.
	 * This provider will be retrieve the values for items such as API Key etc.
	 * @param provider Configuration provider
	 */
	AuthProvider setConfiguration(ConfigurationProvider provider);
	
	Map<String, String> generateAuthHeaders(String requestPayload, String requestUrl, String httpMethod);


}
