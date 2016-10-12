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

import io.aexp.api.client.core.configuration.ConfigurationKeys;
import io.aexp.api.client.core.configuration.ConfigurationProvider;
import io.aexp.api.client.core.configuration.PropertiesConfigurationProvider;


import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SandboxTest {
	
    private ConfigurationProvider configurationProvider;
    private String propertiesFileName = "sandbox.test.properties";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient httpClient;
    private String baseUrl;
      
    @Before
    public void setup() throws IOException {
        PropertiesConfigurationProvider provider = new PropertiesConfigurationProvider();
        ClassLoader classLoader = getClass().getClassLoader();

        provider.loadProperties(classLoader.getResource(propertiesFileName).openStream());
        configurationProvider = provider;
        baseUrl = configurationProvider.getValue(ConfigurationKeys.BASE_URL);

        httpClient = new OkHttpClient.Builder().build();
 
    }
        

    @Ignore("This unit test requires a valid API key")
    @Test
    public void atmlLocatorTest() throws IOException {
        AuthProvider authProvider = ApiKeyAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("ATM_LOCATOR_RESOURCE");
        String method = "GET";
        String payload = null;

        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        queryParams.put("radius_unit", "MI");
        queryParams.put("radius", "5");
        queryParams.put("latitude", "32.67");
        queryParams.put("longitude", "-96.79");
        queryParams.put("limit", "20");
        queryParams.put("offset", "1");
        submitGetRequest(url, authProvider.generateAuthHeaders(payload, url, method), queryParams);
    }
    

    @Ignore("This unit test requires a valid API key")
    @Test
    public void amexTokenizationTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String method = "POST";
        String url = baseUrl + configurationProvider.getValue("ENHANCED_AUTHORIZATION");
        String payload = "{\"timestamp\":\"2013-12-13T11:10:00.715-05:00\",\"transaction_data\":{\"card_number\":\"375987654321001\",\"amount\":\"175.25\",\"timestamp\":\"2013-12-13T11:10:00.715-05:00\",\"currency_code\":\"840\",\"card_acceptor_id\":\"1030026553\",\"is_coupon_used\":\"false\",\"electronic_delivery_email\":\"12user@xyz.com\",\"top5_items_in_cart\":\"00010002000300040005\",\"merchant_product_sku\":\"TKDC315U\",\"shipping_method\":\"02\",\"number_of_gift_cards_in_cart\":\"2\"},\"purchaser_information\":{\"customer_email\":\"customer@wal.com\",\"billing_address\":\"1234 Main Street\",\"billing_postal_code\":\"12345\",\"billing_first_name\":\"Test\",\"billing_last_name\":\"User\",\"billing_phone_number\":\"6028888888\",\"shipto_address\":\"1234 Main Street\",\"shipto_postal_code\":\"12345\",\"shipto_first_name\":\"Test\",\"shipto_last_name\":\"User\",\"shipto_phone_number\":\"6028888888\",\"shipto_country_code\":\"840\",\"latitude_of_customers_device\":\"38.897683\",\"longitude_of_customers_device\":\"-77.036497\",\"device_id\":\"123456789012345678901234567890123456\",\"device_type\":\"01\",\"device_timezone\":\"UTC-07:00\",\"device_ip\":\"10.0.0.0\",\"host_name\":\"PHX.QW.AOL.COM\",\"user_agent\":\"Mozilla\",\"customer_ani\":\"\",\"customer_II_digits\":\"11\"},\"registration_details\":{\"is_registered\":\"true\",\"registered_name\":\"John Smith\",\"registered_email\":\"12user@abc.com\",\"registered_postal_code\":\"123456\",\"registered_address\":\"4712 Good Road\",\"registered_phone\":\"6027777777\",\"count_of_shipto_addresses_on_file\":\"03\",\"registered_account_tenure\":\"720\"},\"registration_details_change_history\":{\"is_registration_updated\":\"1\",\"registered_name\":\"36500\",\"registered_email\":\"1\",\"registered_password\":\"0000036500\",\"registered_postal_code\":\"36500\",\"registered_address\":\"0000036500\",\"registered_phone\":\"0000036500\",\"shipto_address\":\"0000036500\",\"shipto_name\":\"0000036500\"},\"seller_information\":{\"latitude\":\"38.897683\",\"longitude\":\"-77.036497\",\"owner_name\":\"Iam Owner\",\"seller_id\":\"1234567890\",\"business_name\":\"TestITD\",\"tenure\":\"36\",\"transaction_type_indicator\":\"\",\"address\":\"123 Main Street\",\"phone\":\"6021111111\",\"email\":\"user@lmn.com\",\"postal_code\":\"45678\",\"region\":\"USA\",\"country_code\":\"840\"}}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }

    
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void enhancedAuthTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String method = "POST";
        String url = baseUrl + configurationProvider.getValue("ENHANCED_AUTHORIZATION");
        String payload = "{\"timestamp\":\"2013-12-13T11:10:00.715-05:00\",\"transaction_data\":{\"card_number\":\"375987654321001\",\"amount\":\"175.25\",\"timestamp\":\"2013-12-13T11:10:00.715-05:00\",\"currency_code\":\"840\",\"card_acceptor_id\":\"1030026553\",\"is_coupon_used\":\"false\",\"electronic_delivery_email\":\"12user@xyz.com\",\"top5_items_in_cart\":\"00010002000300040005\",\"merchant_product_sku\":\"TKDC315U\",\"shipping_method\":\"02\",\"number_of_gift_cards_in_cart\":\"2\"},\"purchaser_information\":{\"customer_email\":\"customer@wal.com\",\"billing_address\":\"1234 Main Street\",\"billing_postal_code\":\"12345\",\"billing_first_name\":\"Test\",\"billing_last_name\":\"User\",\"billing_phone_number\":\"6028888888\",\"shipto_address\":\"1234 Main Street\",\"shipto_postal_code\":\"12345\",\"shipto_first_name\":\"Test\",\"shipto_last_name\":\"User\",\"shipto_phone_number\":\"6028888888\",\"shipto_country_code\":\"840\",\"latitude_of_customers_device\":\"38.897683\",\"longitude_of_customers_device\":\"-77.036497\",\"device_id\":\"123456789012345678901234567890123456\",\"device_type\":\"01\",\"device_timezone\":\"UTC-07:00\",\"device_ip\":\"10.0.0.0\",\"host_name\":\"PHX.QW.AOL.COM\",\"user_agent\":\"Mozilla\",\"customer_ani\":\"\",\"customer_II_digits\":\"11\"},\"registration_details\":{\"is_registered\":\"true\",\"registered_name\":\"John Smith\",\"registered_email\":\"12user@abc.com\",\"registered_postal_code\":\"123456\",\"registered_address\":\"4712 Good Road\",\"registered_phone\":\"6027777777\",\"count_of_shipto_addresses_on_file\":\"03\",\"registered_account_tenure\":\"720\"},\"registration_details_change_history\":{\"is_registration_updated\":\"1\",\"registered_name\":\"36500\",\"registered_email\":\"1\",\"registered_password\":\"0000036500\",\"registered_postal_code\":\"36500\",\"registered_address\":\"0000036500\",\"registered_phone\":\"0000036500\",\"shipto_address\":\"0000036500\",\"shipto_name\":\"0000036500\"},\"seller_information\":{\"latitude\":\"38.897683\",\"longitude\":\"-77.036497\",\"owner_name\":\"Iam Owner\",\"seller_id\":\"1234567890\",\"business_name\":\"TestITD\",\"tenure\":\"36\",\"transaction_type_indicator\":\"\",\"address\":\"123 Main Street\",\"phone\":\"6021111111\",\"email\":\"user@lmn.com\",\"postal_code\":\"45678\",\"region\":\"USA\",\"country_code\":\"840\"}}";
        Map<String,String> headers = authProvider.generateAuthHeaders(payload, url, method);

        submitPostRequest(url, headers, payload);
    }

    @Ignore("This unit test requires a valid API key")
    @Test
    public void amexCreditCardOffersTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String method = "POST";
        String url = baseUrl + configurationProvider.getValue("AMEX_CARD_OFFERS");
        String payload = "{\"acknowledge_offer\": {\"offer_request_id\":	\"1467840166684U75512110048uMjPR8z\", \"request_timestamp\": \"2012010516024\"}}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }

    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void payWithRewardsTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String method = "POST";
        String url = baseUrl + configurationProvider.getValue("REWARDS_SEARCH");
        String payload = "{\n\"merchant_client_id\":\"Test Merchant\",\n\"timestamp\":\"2015-10-16T00:40:00.715-07:00\",\n\"card\":{\n\"number\":\"375987654321001\"\n}\n}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void acceptAmexTest() throws IOException {
        AuthProvider authProvider = ApiKeyAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String method = "POST";
        String url = baseUrl + configurationProvider.getValue("ACCEPT_AMEX_RESOURCE");
        String payload = "{\"CardTransaction\":{\"MsgTypId\":\"1100\",\"CardNbr\":\"375987654321001\",\"TransProcCd\":\"004800\",\"TransAmt\":\"517\",\"XmitTs\":\"0608113342\",\"MerSysTraceAudNbr\":\"726674\",\"TransTs\":\"160608113342\",\"CardExprDt\":\"1809\",\"AcqInstCtryCd\":\"840\",\"PointOfServiceData\":{\"CardDataInpCpblCd\":\"1\",\"CMAuthnCpblCd\":\"0\",\"CardCptrCpblCd\":\"0\",\"OprEnvirCd\":\"9\",\"CMPresentCd\":\"S\",\"CardPresentCd\":\"0\",\"CardDataInpModeCd\":\"1\",\"CMAuthnMthdCd\":\"0\",\"CMAuthnEnttyCd\":\"0\",\"CardDataOpCpblCd\":\"1\",\"TrmnlOpCpblCd\":\"1\",\"PINCptrCpblCd\":\"0\"},\"MsgRsnCd\":\"1900\",\"MerCtgyCd\":\"4816\",\"AprvCdLgth\":\"6\",\"RtrvRefNbr\":\" 985844055-0\",\"CardAcceptorIdentification\":{\"MerId\":\"5023403389\"},\"TransCurrCd\":\"840\",\"VerificationInformation\":{\"FormNbr\":\"2\",\"ServId\":\"AX\",\"ReqTypId\":\"AD\",\"AddressVerificationData\":{\"CMBillPostCd\":\"EC170155\",\"CMBillAddr\":\"TOLEDO N14-213 Y CAR\",\"CMFirstNm\":\"GUNTHER\",\"CMLastNm\":\"GUTIERREZ\",\"CMPhoneNbr\":\"5939994423\"}}}}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }


    /**
     * Builds query string and submits get request. Unit test succeeds if status code is in [200, 300] range. 
     * Amex requires the TLS 1.2, The test cases contained in this file employ TLS 1.2 please insure to retain the TLS 1.2 standard for any additions.
     * @param url Resource URL
     * @param headers Request headers
     * @param queryParameters Query parameters
     * @throws IOException Exception
     */
    private void submitGetRequest(String url, Map<String, String> headers, Map<String, String> queryParameters)
            throws IOException {
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        for (Map.Entry<String, String> query : queryParameters.entrySet()) {
            httpUrlBuilder.addQueryParameter(query.getKey(), query.getValue());
        }

        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .get();

        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }

        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();

        System.out.print(response.body().string());
        assertTrue(response.isSuccessful());
 
    }

    /**
     * Builds and submits post request. Unit test succeeds if status code is in [200, 300] range.
     * Amex requires the TLS 1.2, The test cases contained in this file employ TLS 1.2 please insure to retain the TLS 1.2 standard for any additions.
     * @param url Resource URL
     * @param headers Request headers
     * @param payload JSON payload
     * @throws IOException Exception
     */
    private void submitPostRequest(String url, Map<String, String> headers, String payload) throws IOException {
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .post(RequestBody.create(JSON_MEDIA_TYPE, payload));

        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println(header.getKey() + " = " + header.getValue());
            builder.addHeader(header.getKey(), header.getValue());
        }
        
        
        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();

        System.out.print("Response: " + response.body().string());
        assertTrue(response.isSuccessful());
    }

    

}