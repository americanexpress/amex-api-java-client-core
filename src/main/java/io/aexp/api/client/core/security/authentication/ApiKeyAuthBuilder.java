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

/**
 * Provides an implementation of AuthProviderBuilder for the API Key only authentication schema.
 */
public class ApiKeyAuthBuilder extends BaseAuthBuilder {

	private ApiKeyAuthBuilder(){
		super();
	}
	
	public static final ApiKeyAuthBuilder getBuilder(){
		return new ApiKeyAuthBuilder();
	}
	

	public AuthProvider build(){
		ApiKeyAuthProvider provider = new ApiKeyAuthProvider();
		provider.setConfiguration(super.getConfiguration());
		return  provider;
	}

}
