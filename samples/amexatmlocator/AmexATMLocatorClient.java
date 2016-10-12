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

package amexatmlocator;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import io.aexp.api.client.core.configuration.PropertiesConfigurationProvider;
import io.aexp.api.client.core.security.authentication.ApiKeyAuthBuilder;
import io.aexp.api.client.core.security.authentication.AuthProvider;

 /**
 * ATM Locator API sup following query parameters are available - 1. limit 2. radius_unit 3.
 * radius 4. longitude 5. offset 6. latitude
 *
 * In addition the following boolean values (Y/N) could be set to narrow search
 * results
 *
 * 7. chip 8. wheelchair_access 9. braille_enabled 10. deposit_accepting 11.
 * no_fee 12. pin 13. restricted_access 14. balance_inquiry 15. contactless 16.
 * multi_currency 17. withdraw_limit 18. mobile_top_up
 *
 * @author AMEX
 *
 */
public class AmexATMLocatorClient {

    private static final String CLIENT_ID = "YOUR CLIENT ID";
	private static final String ATM_LOCATOR_RESOURCE_URL = "https://api.qasb.americanexpress.com/servicing/v1/banks/atms";


    public String search(Map<String, String> queryParams) throws IOException {
    	Properties properties = new Properties();
    	properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("ATM_LOCATOR_RESOURCE_URL", ATM_LOCATOR_RESOURCE_URL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);
        
        AuthProvider authProvider = ApiKeyAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("ATM_LOCATOR_RESOURCE_URL");
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        for (Map.Entry<String, String> query : queryParams.entrySet()) {
            httpUrlBuilder.addQueryParameter(query.getKey(), query.getValue());
        }

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
