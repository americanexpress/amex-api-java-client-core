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

import static org.junit.Assert.*;

import java.io.File;

import io.aexp.api.client.core.configuration.ConfigurationKeys;
import io.aexp.api.client.core.configuration.ConfigurationProvider;
import io.aexp.api.client.core.configuration.PropertiesConfigurationProvider;
import io.aexp.api.client.core.security.Base64;
import io.aexp.api.client.core.security.AuthProviderBuilder;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.net.*;


/**
 *
 */
public class ApiAuthenticationTest {

    private String macAlgorithm = "HmacSHA256";
    private Map<String, String> authHeaders;
    private ConfigurationProvider configurationProvider;
    private String propertiesFileName = "core.test.properties";

    @Before
    public void setup() throws IOException {
        PropertiesConfigurationProvider provider = new PropertiesConfigurationProvider();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(propertiesFileName).getFile());
        provider.loadProperties(file.getAbsolutePath());

        configurationProvider = provider;
    }

    
    @Test
    public void genHmacHeadersForKnownValues() throws Exception {
    	
    	String payload = "The swift brown fox jumped over the lazy dogs back";
    	
        //derived from known values
    	String expected= "MAC id=\"UNIT-TEST-KEY-4388-87b9-85cf463231d7\",ts=\"1473803713478\",nonce=\"f00870f3-5862-45f1-9bd1-ba94c71d2661\",bodyhash=\"wlAalPXGd1oDuqepWDawftGy9zhgEV3oHZve/hz5Yac=\",mac=\"UJePblmmrfot2OufDjeC+Jmdr/8Q0Wrfheccjbp4LMU=\"";
    	
    	HmacAuthProvider prov = (HmacAuthProvider)HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String result = prov.generateMacHeader(configurationProvider.getValue(ConfigurationKeys.CLIENT_KEY),
	    			configurationProvider.getValue(ConfigurationKeys.CLIENT_SECRET), 
	    			"/americanexpress/risk/fraud/v1/enhanced_authorizations/online_purchases", 
	    			"github.com", 443, "POST", 
	    			payload,"f00870f3-5862-45f1-9bd1-ba94c71d2661","1473803713478");
	    	
	    assertTrue(expected.equals(result));
    }
    
    
    
    @Test
    public void genApiKeyHeaders() {

        String payload = null; //GET does not have a payload
        AuthProviderBuilder builder = ApiKeyAuthBuilder.getBuilder();
        AuthProvider authProvider = builder.setConfiguration(configurationProvider).build();
        String targetURL = configurationProvider.getValue(ConfigurationKeys.BASE_URL);
        authHeaders = authProvider.generateAuthHeaders(payload, targetURL, "GET");

        assertEquals(authHeaders.size(), 2);
        assertTrue(authHeaders.containsKey(AuthHeaderNames.X_AMEX_API_KEY));
        assertTrue(authHeaders.containsKey(AuthHeaderNames.X_AMEX_REQUEST_ID));
        assertEquals(authHeaders.get(AuthHeaderNames.X_AMEX_API_KEY), configurationProvider.getValue(ConfigurationKeys.CLIENT_KEY));
        assertNotNull(authHeaders.get(AuthHeaderNames.X_AMEX_REQUEST_ID));

    }


    @Test
    public void genHmacHeaders() throws ParseException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException, MalformedURLException {
        String payload = "{some data}";
        String httpMethod = "GET";
        String clientId = configurationProvider.getValue(ConfigurationKeys.CLIENT_KEY);
        String clientSecret = configurationProvider.getValue(ConfigurationKeys.CLIENT_SECRET);
        String targetURL = configurationProvider.getValue(ConfigurationKeys.BASE_URL);

        AuthProviderBuilder builder = HmacAuthBuilder.getBuilder();
        AuthProvider authProvider = builder.setConfiguration(configurationProvider).build();

        authHeaders = authProvider.generateAuthHeaders(payload, targetURL, httpMethod);


        String pattern = "MAC id=\"{0}\",ts=\"{1}\",nonce=\"{2}\",bodyhash=\"{4}\",mac=\"{3}\"";
        MessageFormat msgFormat = new MessageFormat(pattern);
              
        String header = authHeaders.get(AuthHeaderNames.AUTHORIZATION);
        assertNotNull(header);
        Object[] headers = msgFormat.parse(header);

        assertEquals(5,  headers.length);
        assertEquals(clientId, headers[0]);

        String ts = "" + headers[1];
        String nonce = (String)headers[2];
        String messageHash = (String)headers[4];
        String messageSignature = (String)headers[3];

        validateHMAC(clientSecret, ts, nonce, httpMethod, targetURL, payload, messageHash, messageSignature);
    }


    private void validateHMAC(String secretKey, String timestamp, String nonce, String httpMethod,
                              String url, String body, String messageBodyHash, String messageSignature)
            throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException, MalformedURLException {

            URI uri = new URI(url);
            String resourceUri = uri.getPath();
            String host = uri.getHost().trim().toLowerCase();
            int port = (uri.getPort() == -1) ? uri.toURL().getDefaultPort() : uri.getPort();

            Mac mac = createMacGenerator(secretKey);
            String bodyHash = generateBodyHash(body, mac);
            String macInput = timestamp + "\n" + nonce + "\n" + httpMethod + "\n" + resourceUri + "\n" + host + "\n" + port + "\n" + bodyHash + "\n";

            byte[] rawMacSignature = mac.doFinal(macInput.getBytes());
            String macSignature = new String(Base64.encodeBytes(rawMacSignature));

            assertEquals(messageBodyHash, bodyHash);
            assertEquals(messageSignature, macSignature);

    }

    private String generateBodyHash(String body, Mac mac) {
        byte[] rawBodyHash = mac.doFinal(body.getBytes());
        return new String(Base64.encodeBytes(rawBodyHash));
    }

    private Mac createMacGenerator(String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), macAlgorithm);
        Mac mac = Mac.getInstance(macAlgorithm);
        mac.init(key);
        return mac;
    }
}