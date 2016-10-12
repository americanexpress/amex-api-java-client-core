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

import java.net.URL;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import io.aexp.api.client.core.configuration.ConfigurationKeys;
import io.aexp.api.client.core.security.Base64;

/**
 * Provides the implementation of the Amex specific HMAC algorithm.
 */
public class HmacAuthProvider extends BaseAuthProvider {

	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String SIGNATURE_FORMAT = "%s\n%s\n%s\n%s\n%s\n%s\n%s\n";
    private static final String AUTH_HEADER_FORMAT = "MAC id=\"%s\",ts=\"%s\",nonce=\"%s\",bodyhash=\"%s\",mac=\"%s\"";
	private static final String UTF8_ENCODING = "UTF-8";

 /**
  * Generates the Amex specific authentication headers required to support the HMCA authentication schema.
  * The authentication header will be calculated based on the current Amex HMAC algorithm. 
  * The API key header will be populated by the configuration provider supplied. 
  * The request ID header will be populated with a GUID.
  */
	public Map<String, String> generateAuthHeaders(String reqPayload, String requestUrl, String httpMethod) {
		String payload;
		URL url;
		String resourcePath;
		String host;
		String macAuth;
		int port;
		Map<String, String> headers = new Hashtable<String, String>();
		try {
			url = new URL(requestUrl);
			resourcePath = url.getPath();
			host = url.getHost().trim().toLowerCase();
			port = (url.getPort() == -1) ? url.getDefaultPort() : url.getPort();
			payload = (reqPayload == null)? "" : reqPayload;
			macAuth = generateMacHeader(getConfigurationValue(ConfigurationKeys.CLIENT_KEY),
					getConfigurationValue(ConfigurationKeys.CLIENT_SECRET), resourcePath, host, port, httpMethod, payload);
			headers.put(AuthHeaderNames.AUTHORIZATION, macAuth);
			headers.put(AuthHeaderNames.X_AMEX_API_KEY, getConfigurationValue(ConfigurationKeys.CLIENT_KEY));
			headers.put(AuthHeaderNames.X_AMEX_REQUEST_ID, getRequestUUID());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return Collections.unmodifiableMap(headers);
	}

	final String generateMacHeader(String client_id,
			String client_secret, String resourcePath, String host, int port,
			String httpMethod, String payload) throws Exception {
		String nonce = UUID.randomUUID().toString();
		String ts = "" + System.currentTimeMillis();

		return generateMacHeader(client_id, client_secret, resourcePath, host, port, httpMethod, payload, nonce, ts);
	}
	
	final String generateMacHeader(String client_id,
			String client_secret, String resourcePath, String host, int port,
			String httpMethod, String payload, String nonce, String ts) throws Exception {
		SecretKeySpec signingKey = new SecretKeySpec(
				client_secret.getBytes(UTF8_ENCODING), HMAC_SHA256_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(signingKey);
		
		// create the bodyHash value by hashing the payload and encoding it
		byte[] rawBodyHash = mac.doFinal(payload.getBytes(UTF8_ENCODING));
		String bodyHash = Base64.encodeBytes(rawBodyHash);

		//The order is CRITICAL! 
		//Timestamp + \n + nonce + \n+ httpmethod + \n + path + \n +host + \n + port + \n +hash + \n
		String signature = String.format(SIGNATURE_FORMAT, ts, nonce, httpMethod, resourcePath, host, port, bodyHash);

		// Generate signature using client secret (crypto initialized above)
		byte[] signatureBytes = mac.doFinal(signature.getBytes(UTF8_ENCODING));

		// now encode the cypher for the web
		String signatureStr = Base64.encodeBytes(signatureBytes);
		String mac_authorization_header = String.format(AUTH_HEADER_FORMAT, client_id, ts, nonce, bodyHash, signatureStr);
		return mac_authorization_header;
	}
}
