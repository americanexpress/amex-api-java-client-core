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


package paywithrewards;

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

public class PayWithRewardsClient {

	private static final String CLIENT_ID = "YOUR CLIENT ID";
	private static final String CLIENT_SECRET = "YOUR CLIENT SECRET_KEY";
	private static final String SEARCH_REWARDS_RESOURCE_URL = "https://api.qasb.americanexpress.com/risk/fraud/v1/enhanced_authorizations/online_purchases";
	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
	
    public String searchRewards(String searchPayload) throws IOException {
    	Properties properties = new Properties();
    	properties.put("CLIENT_KEY", CLIENT_ID);
    	properties.put("CLIENT_SECRET", CLIENT_SECRET);
        properties.put("REWARDS_SEARCH_RESOURCE_URL", SEARCH_REWARDS_RESOURCE_URL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);
        
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("REWARDS_SEARCH_RESOURCE_URL");
        
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        // This generates the AMEX specific authentication headers needed for this API.
        Map<String, String> headers = authProvider.generateAuthHeaders(searchPayload, url, "POST");
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, searchPayload);
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

}
