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


package amexb2bpayments.reconciliation;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import io.aexp.api.client.core.configuration.PropertiesConfigurationProvider;
import io.aexp.api.client.core.security.authentication.AuthProvider;
import io.aexp.api.client.core.security.authentication.HmacAuthBuilder;

public class AmexB2BReconcileClient {

	private static final String CLIENT_ID = "YOUR CLIENT ID";
	private static final String CLIENT_SECRET = "YOUR CLIENT SECRET_KEY";
	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
	
	private static final String ORGANIZATION_STATUS_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations/%s/status";
	private static final String ACCOUNT_STATUS_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations/%s/accounts/%s/status";
	private static final String PAYMENT_STATUS_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations/%s/payments/%s/status";
	private static final String ORG_EXCEPTION_RESOURCE_URL = "https://api.qasb.americanexpress.com/b2bcommerce/v2/organizations/%s/exceptions";
	
	public void reconcileClientRequests(String organizationId, String accountId, String paymentId) {
		String orgStatusURL = String.format(ORGANIZATION_STATUS_RESOURCE_URL, organizationId);
		String accStatusURL = String.format(ACCOUNT_STATUS_RESOURCE_URL, organizationId, accountId);	
		String payStatusURL = String.format(PAYMENT_STATUS_RESOURCE_URL, organizationId, paymentId);
		String orgExceptionURL = String.format(ORG_EXCEPTION_RESOURCE_URL, organizationId);
		
		Properties properties = new Properties();
    	properties.put("CLIENT_KEY", CLIENT_ID);
    	properties.put("CLIENT_SECRET", CLIENT_SECRET);
    	
    	properties.put("ORGANIZATION_STATUS_RESOURCE_URL", orgStatusURL);
    	properties.put("ACCOUNT_STATUS_RESOURCE_URL", accStatusURL);
    	properties.put("PAYMENT_STATUS_RESOURCE_URL", payStatusURL);
    	properties.put("ORG_EXCEPTION_RESOURCE_URL", orgExceptionURL);
    	
    	String orgStatusResp = sendReconcileRequest(properties, "ORGANIZATION_STATUS_RESOURCE_URL");
    	String accountStatusResp = sendReconcileRequest(properties, "ACCOUNT_STATUS_RESOURCE_URL");
    	String payStatusResp = sendReconcileRequest(properties, "PAYMENT_STATUS_RESOURCE_URL");
    	String orgExceptionResp = sendReconcileRequest(properties, "ORG_EXCEPTION_RESOURCE_URL");
	}
	
	public String sendReconcileRequest(Properties properties, String urlConstant) {
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);
        
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue(urlConstant);
        
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        // This generates the AMEX specific authentication headers needed for this API.
        Map<String, String> headers = authProvider.generateAuthHeaders(null, url, "GET");
        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .get();
                
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
	}
}
