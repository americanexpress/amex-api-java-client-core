/*
 * Copyright (c) 2017 American Express Travel Related Services Company, Inc.
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


package amexb2bpayments.onboarding;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import io.aexp.api.client.core.configuration.PropertiesConfigurationProvider;
import io.aexp.api.client.core.security.authentication.AuthProvider;
import io.aexp.api.client.core.security.authentication.HmacAuthBuilder;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Client for American Express B2B onboarding requests. Client only has samples for organization but the
 * B2B onboarding for accounts is the essentially the same as organization, just with different URL and payload.
 */
public class AmexB2BOnboardingClient {
	
	private static final String CLIENT_ID = "YOUR CLIENT ID";
	private static final String CLIENT_SECRET = "YOUR CLIENT SECRET_KEY";
	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
	
	private static final String ENROLL_ORG_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations";
	private static final String UPDATE_ORG_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations/%s";
	private static final String DELETE_ORG_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations/%s";

    public String enrollOrganization(String enrollOrgPayload) throws IOException {
    	Properties properties = new Properties();
		properties.put("CLIENT_KEY", CLIENT_ID);
		properties.put("CLIENT_SECRET", CLIENT_SECRET);
		properties.put("ENROLL_ORG_RESOURCE_URL", ENROLL_ORG_RESOURCE_URL);
		PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
		configurationProvider.setProperties(properties);
		
		AuthProvider authProvider = HmacAuthBuilder.getBuilder()
		        .setConfiguration(configurationProvider)
		        .build();
		
		String url = configurationProvider.getValue("ENROLL_ORG_RESOURCE_URL");
		
		HttpUrl httpUrl = HttpUrl.parse(url);
		HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
		
		// This generates the AMEX specific authentication headers needed for this API.
		Map<String, String> headers = authProvider.generateAuthHeaders(enrollOrgPayload, url, "POST");
		RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, enrollOrgPayload);
		Request.Builder builder = new Request.Builder()
		        .url(httpUrlBuilder.build())
		        .post(body);
		        
		for (Map.Entry<String, String> header : headers.entrySet()) {
		    builder.addHeader(header.getKey(), header.getValue());
		}
		Request request = builder.build();
		
		OkHttpClient httpClient = new OkHttpClient.Builder().build();
		Response response = httpClient.newCall(request).execute();
		return response.body().string();
	}
	 
    public String updateOrganization(String updateOrgPayload, String organizationId) throws IOException {
    	String updateOrgURL = String.format(UPDATE_ORG_RESOURCE_URL, organizationId);
    	
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
		properties.put("CLIENT_SECRET", CLIENT_SECRET);
		properties.put("UPDATE_ORG_RESOURCE_URL", updateOrgURL);
		PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
		configurationProvider.setProperties(properties);
		
		AuthProvider authProvider = HmacAuthBuilder.getBuilder()
		        .setConfiguration(configurationProvider)
		        .build();
		
		String url = configurationProvider.getValue("UPDATE_ORG_RESOURCE_URL");
		
		HttpUrl httpUrl = HttpUrl.parse(url);
		HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
		
		// This generates the AMEX specific authentication headers needed for this API.
		Map<String, String> headers = authProvider.generateAuthHeaders(updateOrgPayload, url, "PUT");
	    RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, updateOrgPayload);
	    Request.Builder builder = new Request.Builder()
	            .url(httpUrlBuilder.build())
	            .put(body);
	            
	    for (Map.Entry<String, String> header : headers.entrySet()) {
	        builder.addHeader(header.getKey(), header.getValue());
	    }
	    Request request = builder.build();
	
	    OkHttpClient httpClient = new OkHttpClient.Builder().build();
	    Response response = httpClient.newCall(request).execute();
	    return response.body().string();
    }
	 
	public String deleteOrganization(String organizationId) throws IOException {
		String deleteOrgURL = String.format(DELETE_ORG_RESOURCE_URL, organizationId);
		
	    Properties properties = new Properties();
    	properties.put("CLIENT_KEY", CLIENT_ID);
    	properties.put("CLIENT_SECRET", CLIENT_SECRET);
        properties.put("DELETE_ORG_RESOURCE_URL", deleteOrgURL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);
        
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("DELETE_ORG_RESOURCE_URL");
        
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        // This generates the AMEX specific authentication headers needed for this API.
        Map<String, String> headers = authProvider.generateAuthHeaders(null, url, "DELETE");
        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .delete();
                
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
}
