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
    public void atmLocatorTest() throws IOException {
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
    public void organizationStatusTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("ORGANIZATION_STATUS_RESOURCE");
        String method = "GET";
        String payload = null;
        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        submitGetRequest(url, authProvider.generateAuthHeaders(payload, url, method), queryParams);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void accountStatusTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("ACCOUNT_STATUS_RESOURCE");
        String method = "GET";
        String payload = null;
        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        submitGetRequest(url, authProvider.generateAuthHeaders(payload, url, method), queryParams);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void paymentStatusTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("PAYMENT_STATUS_RESOURCE");
        String method = "GET";
        String payload = null;
        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        submitGetRequest(url, authProvider.generateAuthHeaders(payload, url, method), queryParams);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void organizationExceptionTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("ORGANIZATION_EXCEPTION_RESOURCE");
        String method = "GET";
        String payload = null;
        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        submitGetRequest(url, authProvider.generateAuthHeaders(payload, url, method), queryParams);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void createPaymentTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("CREATE_PAYMENT_RESOURCE");
        String method = "POST";
        String payload = "{\n  \"payee_id\": \"OSKzWacFQ5mef07R\",\n  \"payer_pymt_ref_id\": \"INVOICE1231\",\n  \"payment_value\": \"892.91\",\n  \"currency_code\": \"USD\",\n  \"payment_due_date\": \"20160923\",\n  \"payment_method\": \"CH\",\n  \"check_payment\": {\n    \"payer_account_id\": \"AB1v6pb1eagWUkt2\",\n    \"check_no\": \"16556511\",\n    \"check_date\": \"20160923\",\n    \"check_memo\": \"Payment\",\n    \"payee_name\": \"WALMART\",\n    \"mail_deadline\": \"S\",\n    \"check_delivery_details\": {\n      \"delivery_indicator\": \"1000\",\n      \"mail_vendor_method_code\": \"USPS FULL\",\n      \"mail_vendor_account_no\": \"AT7676\"\n    },\n    \"address\": [\n      {\n        \"id\": \"908908098\",\n        \"type\": \"Check_Destination\",\n        \"address_line_1\": \"3232 bell rd\",\n        \"address_line_2\": \"SUITE 45334\",\n        \"address_line_3\": null,\n        \"address_line_4\": null,\n        \"address_line_5\": null,\n        \"city\": \"Phoenix\",\n        \"region_code\": \"AZ\",\n        \"region_name\": \"Arizona\",\n        \"country_code\": \"US\",\n        \"country_name\": \"USA\",\n        \"zip_code\": \"85302\"\n      }\n    ],\n    \"company_affiliate\": {\n      \"id\": \"CA357656\",\n      \"type\": \"Contact\",\n      \"title\": \"Executive\",\n      \"salutation\": \"Mr\",\n      \"first_name\": \"Michael\",\n      \"middle_name\": null,\n      \"last_name\": \"Johnson\",\n      \"email_id\": \"michael.johnson@walmart.com\",\n      \"phones\": [\n        {\n          \"number\": \"6023490200\",\n          \"extension\": \"40411\",\n          \"type\": \"business\"\n        }\n      ],\n      \"fax_no\": \"3434344565\"\n    }\n  },\n  \"attachments\": [\n    {\n      \"id\": \"Invoice1.pdf\",\n      \"file\": \"B9DQogICAgfQ0KfQ==\"\n    }\n  ],\n  \"invoices\": [\n    {\n      \"id\": \"Payment1122\",\n      \"date\": \"20160903\",\n      \"due_date\": \"20160923\",\n      \"payee_ref\": \"INVX00222\",\n      \"payer_ref\": \"WORKORDER912\",\n      \"paid_amount\": \"892.91\",\n      \"gross_amount\": \"892.91\",\n      \"net_value\": \"892.91\",\n      \"tax_code\": \"3\",\n      \"tax_value\": \"0.01\",\n      \"freight_value\": \"0.01\",\n      \"discount_value\": \"0.15\",\n      \"adjustment_value\": \"0.11\",\n      \"payer_field_1\": \"Michael\",\n      \"payer_field_2\": null,\n      \"payer_field_3\": null,\n      \"payer_field_4\": null,\n      \"short_pay_code\": \"T3\",\n      \"short_pay_text\": \"forSOAP\",\n      \"short_pay_value\": \"892.91\",\n      \"additional_text\": \"for retail of SOAP\",\n      \"line_items\": [\n        {\n          \"line_number\": \"121\",\n          \"line_description\": \"for soap\",\n          \"line_paid_value\": \"892.91\",\n          \"purchase_order_item\": \"18\",\n          \"part_number\": \"B200\",\n          \"unit_price\": \"300\",\n          \"quantity\": \"5\",\n          \"unit_of_measure\": \"EA\",\n          \"line_value\": \"1500\",\n          \"purchase_order\": \"P6405\",\n          \"tax_code\": \"3\",\n          \"tax_jurisdiction\": \"State\",\n          \"line_freight_value\": \"0.91\",\n          \"line_discount_value\": \"0.18\",\n          \"line_adjustment_value\": \"0.10\",\n          \"payer_field_1\": \"Michael\",\n          \"payer_field_2\": null,\n          \"payer_field_3\": null,\n          \"payer_field_4\": null,\n          \"short_pay_code\": \"FE\",\n          \"short_pay_text\": \"FreightExceedsPOMaximum\",\n          \"short_pay_value\": \"140\"\n        }\n      ]\n    }\n  ],\n  \"risk_management_details\": {\n    \"user_id\": \"WAL232\",\n    \"title\": \"Executive\",\n    \"salutation\": \"Mr\",\n    \"first_name\": \"James\",\n    \"middle_name\": null,\n    \"last_name\": \"Philip\",\n    \"email_id\": \"james.philip@aexp.com\",\n    \"phones\": [\n      {\n        \"number\": \"6023490200\",\n        \"extension\": \"404\",\n        \"type\": \"business\"\n      }\n    ],\n    \"fax_no\": \"3434344565\",\n    \"user_count\": 0,\n    \"transaction_count\": 50,\n    \"login_volume\": 10,\n    \"user_tenure\": \"01/02/2015\",\n    \"ip_address\": \"10.235.11.11\",\n    \"device_id\": \"D23232323\",\n    \"session_id\": \"NE625256\",\n    \"submit_date\": \"01/02/2015 12:15:12\",\n    \"user_agent\": {\n      \"web_action_path\": \"Mozilla/5.0 (Windows NT 6.1; / 20100101 Firefox/ 12.0 \",\n      \"browser_type\": \"Firefox\",\n      \"browser_version\": \"12.0\"\n    },\n    \"payer_agent_email_ids\": [\n      {\n        \"payer_agent_email_id\": \"Michale@walmart.com\"\n      }\n    ]\n  }\n}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void enrollOrganizationTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("ENROLL_ORG_RESOURCE");
        String method = "POST";
        String payload = "{\n  \"name\": \"Johnson Company\",\n  \"type\": \"Payee\",\n  \"associated_payer_id\": null,\n  \"short_name\": \"Johnson\",\n  \"email_id\": \"John@Johnson.com\",\n  \"business_website\": \"http://www.Johnson.com\",\n  \"parent_customer_id\": null,\n  \"addresses\": [\n    {\n      \"id\": \"AFWsZurBRfD22Hca\",\n      \"type\": \"primary\",\n      \"address_line_1\": \"300 Park Ave\",\n      \"address_line_2\": \"update testing\",\n      \"address_line_3\": null,\n      \"address_line_4\": null,\n      \"address_line_5\": null,\n      \"city\": \"San Jose\",\n      \"region_code\": \"CA\",\n      \"region_name\": \"California\",\n      \"country_code\": \"US\",\n      \"country_name\": \"USA\",\n      \"zip_code\": \"95110\"\n    }\n  ],\n  \"company_affiliates\": [\n    {\n      \"id\": \"AFWsZurBRfD22Hca\",\n      \"type\": \"Contact\",\n      \"title\": \"Executive\",\n      \"salutation\": \"Mr\",\n      \"first_name\": \"James\",\n      \"middle_name\": \"M\",\n      \"last_name\": \"Philip\",\n      \"email_id\": \"james.philip@Johnson.com\",\n      \"phones\": [\n        {\n          \"number\": \"6023490200\",\n          \"extension\": \"404\",\n          \"type\": \"business\"\n        }\n      ],\n      \"fax_no\": \"3434344565\"\n    }\n  ],\n  \"risk_management_details\": {\n    \"user_id\": \"A232\",\n    \"title\": \"Executive\",\n    \"salutation\": \"Mr\",\n    \"first_name\": \"James\",\n    \"middle_name\": \"M\",\n    \"last_name\": \"Philip\",\n    \"email_id\": \"james.philip@johnson.com\",\n    \"phones\": [\n      {\n        \"number\": 6023490200,\n        \"extension\": 404,\n        \"type\": \"business\"\n      }\n    ],\n    \"fax_no\": 3434344565,\n    \"user_count\": 50,\n    \"transaction_count\": 44,\n    \"first_transaction_date\": \"01/02/2015\",\n    \"login_volume\": 10,\n    \"user_tenure\": \"01/02/2015\",\n    \"avg_num_of_fx_pymts\": 50,\n    \"avg_fx_pymt_amt\": 5000,\n    \"goods_and_services\": \"Purchasing from Suppliers\",\n    \"ip_address\": \"10.235.11.11\",\n    \"device_id\": \"D23232323\",\n    \"session_id\": \"lit3py55t21z5v55vlm25s55\",\n    \"submit_date\": \"01/02/2015 12:15:12\",\n    \"user_agent\": {\n      \"web_action_path\": \"Mozilla/5.0 (Windows NT 6.1) Gecko/20100101 Firefox/ 12.0 \",\n      \"browser_type\": \"Firefox\",\n      \"browser_version\": \"12.0\"\n    }\n  }\n}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void updateOrganizationTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("UPDATE_ORG_RESOURCE");
        String method = "PUT";
        String payload = "{\n \"name\": \"Johnson Company\",\n \"type\": \"Payer\",\n \"partner_org_id\": \"abc123\",\n \"associated_payer_id\": null,\n \"doing_business_as_name\": \"ABC INC\",\n \"legal_name\": \"ABC\",\n \"short_name\": \"Johnson\",\n \"phone_no\": \"6023232434\",\n \"tax_id\": \"99933333\",\n \"email_id\": \"John@Johnson.com\",\n \"business_website\": \"http://www.Johnson.com\",\n \"parent_customer_id\": null,\n \"payee_fee_ind\": \"Y\",\n \"payee_tenure\": \"01/15/2015\",\n \"addresses\": [\n {\n \"id\": \"AFWsZurBRfD22Hca\",\n \"type\": \"primary\",\n \"address_line_1\": \"300 Park Ave\",\n \"address_line_2\": \"update testing\",\n \"address_line_3\": null,\n \"address_line_4\": null,\n \"address_line_5\": null,\n \"city\": \"San Jose\",\n \"region_code\": \"CA\",\n \"region_name\": \"California\",\n \"country_code\": \"US\",\n \"country_name\": \"USA\",\n \"zip_code\": \"95110\"\n }\n ],\n \"company_affiliates\": [\n {\n \"id\": \"AFWsZurBRfD22Hca\",\n \"type\": \"Contact\",\n \"title\": \"Executive\",\n \"salutation\": \"Mr\",\n \"first_name\": \"James\",\n \"middle_name\": \"M\",\n \"last_name\": \"Philip\",\n \"email_id\": \"james.philip@Johnson.com\",\n \"phones\": [\n {\n \"number\": \"6023490200\",\n \"extension\": \"404\",\n \"type\": \"business\"\n }\n ],\n \"fax_no\": \"3434344565\"\n }\n ]\n}";
        submitPutRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void deleteOrganizationTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("DELETE_ORG_RESOURCE");
        String method = "DELETE";
        String payload = "";
        submitDeleteRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void enrollAccountTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("ENROLL_ACCOUNT_RESOURCE");
        String method = "POST";
        String payload = "{\n \"account_details\": {\n \"check_accts\": [\n {\n \"bank\": {\n \"name\": \"BankOfAmerica\",\n \"account_holder_name\": \"Michael\",\n \"account_number\": \"2354665565\",\n \"routing_number\": \"021000021\",\n \"acct_type\": \"chk\",\n \"address\": {\n \"type\": \"bank\",\n \"address_line_1\": \"3232 Bell Rd\",\n \"address_line_2\": \"update testing\",\n \"address_line_3\": null,\n \"address_line_4\": null,\n \"address_line_5\": null,\n \"city\": \"Phoenix\",\n \"region_code\": \"AZ\",\n \"region_name\": \"Arizona\",\n \"country_code\": \"US\",\n \"country_name\": \"USA\",\n \"zip_code\": \"85302\"\n }\n },\n \"enable_threshold\": \"Y\",\n \"check_layout_indicator\": \"file1gif\",\n \"check_layout_image\": \"ew0KICAgICJtYW5hZ2VFbnJvbGxtZW50UmVxdWVzdCI6IHsNCiAgICAgICAgImRhdGEiOiB7DQogICAgICAgICAgICAiY29tbW9uUmVxdWVzdENvbnRleHQiOiB7DQogICAgICAgICAgICAgICAgInJlcXVlc3RJZCI6ICJBQTM0MzQzNDIzMjMiLA0KICAgICAgICAgICAgICAgICJwYXJ0bmVySWQiOiAiR1M2NDBCRzAwMSIsDQogICAgICAgICAgICAgICAgInBhcnRuZXJOYW1lIjogIklOVEFDQ1QiLA0KICAgICAgICAgICAgICAgICJ0aW1lc3RhbXAiOiAiMjAxMy0xMi0xM1QxMToxMDowMC43MTUtMDU6MDAiDQogICAgICAgICAgICB9LA0KICAgICAgICAgICAgImVucm9sbG1lbnRBY3Rpb25UeXBlIjogIkFERCIsDQogICAgICAgICAgICAiZW5yb2xsbWVudENhdGVnb3J5IjogIjEiLA0KICAgICAgICAgICAgIm9yZ2FuaXphdGlvbklkIjogIk9SRzMyNDIyIiwNCiAgICAgICAgICAgICJvcmdhbml6YXRpb25JbmZvIjogew0KICAgICAgICAgICAgICAgICJwYXJlbnRPcmdJZCI6ICJCMzIzMzIiLA0KICAgICAgICAgICAgICAgICJvcmdHcm91cHMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJvcmdHcm91cE5hbWUiOiAiTWlkZGxlTWFya2V0Ig0KICAgICAgICAgICAgICAgICAgICB9DQogICAgICAgICAgICAgICAgXSwNCiAgICAgICAgICAgICAgICAic3Vic2NyaWJlU2VydmljZXMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJzZXJ2aWNlQ29kZSI6ICIxIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAic2VydmljZUNvZGUiOiAiMyINCiAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgIF0sDQogICAgICAgICAgICAgICAgIm9yZ05hbWUiOiAiQVBQTEVDT1JQIiwNCiAgICAgICAgICAgICAgICAib3JnU2hvcnROYW1lIjogIkFQUExFIiwNCiAgICAgICAgICAgICAgICAidGF4SWQiOiAiOTk5MzMzMzMiLA0KICAgICAgICAgICAgICAgICJjb250YWN0RGV0YWlscyI6IFsNCiAgICAgICAgICAgICAgICAgICAgew0KICAgICAgICAgICAgICAgICAgICAgICAgImpvYlRpdGxlIjogIiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic2FsdXRhdGlvbiI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImZpcnN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1pZGRsZU5hbWUiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJsYXN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImVtYWlsSWQiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJwcmltYXJ5UGhvbmVObyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1vYmlsZVBob25lTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJleHRlbnNpb24iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJmYXhObyI6ICIiDQogICAgICAgICAgICAgICAgICAgIH0NCiAgICAgICAgICAgICAgICBdLA0KICAgICAgICAgICAgICAgICJvcmdBZGRyZXNzIjogew0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmUxIjogIjMyMzIgQkVMTCBSRCIsDQogICAgICAgICAgICAgICAgICAgICJhZGRyZXNzTGluZTIiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgImFkZHJlc3NMaW5lMyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmU0IjogIiIsDQogICAgICAgICAgICAgICAgICAgICJjaXR5IjogIlBIT0VOSVgiLA0KICAgICAgICAgICAgICAgICAgICAic3RhdGUiOiAiQVoiLA0KICAgICAgICAgICAgICAgICAgICAiY291bnRyeSI6ICJVU0EiLA0KICAgICAgICAgICAgICAgICAgICAiemlwQ29kZSI6ICI4NTAzMiINCiAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICJvcmdDaGVja0RldGFpbHMiOiB7DQogICAgICAgICAgICAgICAgICAgICJjaGVja1NldHRpbmdzIjogew0KICAgICAgICAgICAgICAgICAgICAgICAgImN1c3RGZWVCaWxsSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrTnVtYmVyaW5nSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrUHJpbnRTZXJ2aWNlSW5kIjogIlMiLA0KICAgICAgICAgICAgICAgICAgICAgICAgInVzZUZhc3RGb3J3YXJkU2VydmljZUluZCI6ICJGRiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic3VwcGxpZXJOb3RpZmljYXRpb25JbmQiOiAiIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICAiY2hlY2tEZWxpdmVyeUluZGljYXRvciI6IHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yTWV0aG9kQ29kZSI6ICJGRURFWCAyREFZIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJkZWxpdmVyeUluZGljYXRvciI6ICIxMDAwIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yQWNjTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJob2xkSW5kIjogIk4iDQogICAgICAgICAgICAgICAgICAgIH0sDQogICAgICAgICAgICAgICAgICAgICJwYXlvckF0dGFjaG1lbnQiOiBbDQogICAgICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF0dGFjaG1lbnRJZCI6ICJBVFRBQ0gxMjIxIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiYXR0YWNobWVudEZpbGUiOiAiIg0KICAgICAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgICAgICBdDQogICAgICAgICAgICAgICAgfSwNCiAgICAgICAgICAgICAgICAib3JnQWNoRGV0YWlscyI6IG51bGwsDQogICAgICAgICAgICAgICAgIm9yZ0NhcmREZXRhaWxzIjogbnVsbCwNCiAgICAgICAgICAgICAgICAib3JnSVdpcmVEZXRhaWxzIjogbnVsbA0KICAgICAgICAgICAgfQ0KICAgICAgICB9DQogICAgfQ0KfQ==\",\n \"check_layout_fileType\": null,\n \"logo_indicator\": \"file2gif\",\n \"logo_image\": \"ew0KICAgICJtYW5hZ2VFbnJvbGxtZW50UmVxdWVzdCI6IHsNCiAgICAgICAgImRhdGEiOiB7DQogICAgICAgICAgICAiY29tbW9uUmVxdWVzdENvbnRleHQiOiB7DQogICAgICAgICAgICAgICAgInJlcXVlc3RJZCI6ICJBQTM0MzQzNDIzMjMiLA0KICAgICAgICAgICAgICAgICJwYXJ0bmVySWQiOiAiR1M2NDBCRzAwMSIsDQogICAgICAgICAgICAgICAgInBhcnRuZXJOYW1lIjogIklOVEFDQ1QiLA0KICAgICAgICAgICAgICAgICJ0aW1lc3RhbXAiOiAiMjAxMy0xMi0xM1QxMToxMDowMC43MTUtMDU6MDAiDQogICAgICAgICAgICB9LA0KICAgICAgICAgICAgImVucm9sbG1lbnRBY3Rpb25UeXBlIjogIkFERCIsDQogICAgICAgICAgICAiZW5yb2xsbWVudENhdGVnb3J5IjogIjEiLA0KICAgICAgICAgICAgIm9yZ2FuaXphdGlvbklkIjogIk9SRzMyNDIyIiwNCiAgICAgICAgICAgICJvcmdhbml6YXRpb25JbmZvIjogew0KICAgICAgICAgICAgICAgICJwYXJlbnRPcmdJZCI6ICJCMzIzMzIiLA0KICAgICAgICAgICAgICAgICJvcmdHcm91cHMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJvcmdHcm91cE5hbWUiOiAiTWlkZGxlTWFya2V0Ig0KICAgICAgICAgICAgICAgICAgICB9DQogICAgICAgICAgICAgICAgXSwNCiAgICAgICAgICAgICAgICAic3Vic2NyaWJlU2VydmljZXMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJzZXJ2aWNlQ29kZSI6ICIxIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAic2VydmljZUNvZGUiOiAiMyINCiAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgIF0sDQogICAgICAgICAgICAgICAgIm9yZ05hbWUiOiAiQVBQTEVDT1JQIiwNCiAgICAgICAgICAgICAgICAib3JnU2hvcnROYW1lIjogIkFQUExFIiwNCiAgICAgICAgICAgICAgICAidGF4SWQiOiAiOTk5MzMzMzMiLA0KICAgICAgICAgICAgICAgICJjb250YWN0RGV0YWlscyI6IFsNCiAgICAgICAgICAgICAgICAgICAgew0KICAgICAgICAgICAgICAgICAgICAgICAgImpvYlRpdGxlIjogIiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic2FsdXRhdGlvbiI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImZpcnN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1pZGRsZU5hbWUiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJsYXN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImVtYWlsSWQiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJwcmltYXJ5UGhvbmVObyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1vYmlsZVBob25lTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJleHRlbnNpb24iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJmYXhObyI6ICIiDQogICAgICAgICAgICAgICAgICAgIH0NCiAgICAgICAgICAgICAgICBdLA0KICAgICAgICAgICAgICAgICJvcmdBZGRyZXNzIjogew0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmUxIjogIjMyMzIgQkVMTCBSRCIsDQogICAgICAgICAgICAgICAgICAgICJhZGRyZXNzTGluZTIiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgImFkZHJlc3NMaW5lMyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmU0IjogIiIsDQogICAgICAgICAgICAgICAgICAgICJjaXR5IjogIlBIT0VOSVgiLA0KICAgICAgICAgICAgICAgICAgICAic3RhdGUiOiAiQVoiLA0KICAgICAgICAgICAgICAgICAgICAiY291bnRyeSI6ICJVU0EiLA0KICAgICAgICAgICAgICAgICAgICAiemlwQ29kZSI6ICI4NTAzMiINCiAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICJvcmdDaGVja0RldGFpbHMiOiB7DQogICAgICAgICAgICAgICAgICAgICJjaGVja1NldHRpbmdzIjogew0KICAgICAgICAgICAgICAgICAgICAgICAgImN1c3RGZWVCaWxsSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrTnVtYmVyaW5nSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrUHJpbnRTZXJ2aWNlSW5kIjogIlMiLA0KICAgICAgICAgICAgICAgICAgICAgICAgInVzZUZhc3RGb3J3YXJkU2VydmljZUluZCI6ICJGRiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic3VwcGxpZXJOb3RpZmljYXRpb25JbmQiOiAiIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICAiY2hlY2tEZWxpdmVyeUluZGljYXRvciI6IHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yTWV0aG9kQ29kZSI6ICJGRURFWCAyREFZIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJkZWxpdmVyeUluZGljYXRvciI6ICIxMDAwIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yQWNjTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJob2xkSW5kIjogIk4iDQogICAgICAgICAgICAgICAgICAgIH0sDQogICAgICAgICAgICAgICAgICAgICJwYXlvckF0dGFjaG1lbnQiOiBbDQogICAgICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF0dGFjaG1lbnRJZCI6ICJBVFRBQ0gxMjIxIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiYXR0YWNobWVudEZpbGUiOiAiIg0KICAgICAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgICAgICBdDQogICAgICAgICAgICAgICAgfSwNCiAgICAgICAgICAgICAgICAib3JnQWNoRGV0YWlscyI6IG51bGwsDQogICAgICAgICAgICAgICAgIm9yZ0NhcmREZXRhaWxzIjogbnVsbCwNCiAgICAgICAgICAgICAgICAib3JnSVdpcmVEZXRhaWxzIjogbnVsbA0KICAgICAgICAgICAgfQ0KICAgICAgICB9DQogICAgfQ0KfQ==\",\n \"logo_fileType\": null,\n \"signature_details\": [\n {\n \"threshold_limit\": \"999999.90\",\n \"signature1_image\": \"ew0KICAgICJtYW5hZ2VFbnJvbGxtZW50UmVxdWVzdCI6IHsNCiAgICAgICAgImRhdGEiOiB7DQogICAgICAgICAgICAiY29tbW9uUmVxdWVzdENvbnRleHQiOiB7DQogICAgICAgICAgICAgICAgInJlcXVlc3RJZCI6ICJBQTM0MzQzNDIzMjMiLA0KICAgICAgICAgICAgICAgICJwYXJ0bmVySWQiOiAiR1M2NDBCRzAwMSIsDQogICAgICAgICAgICAgICAgInBhcnRuZXJOYW1lIjogIklOVEFDQ1QiLA0KICAgICAgICAgICAgICAgICJ0aW1lc3RhbXAiOiAiMjAxMy0xMi0xM1QxMToxMDowMC43MTUtMDU6MDAiDQogICAgICAgICAgICB9LA0KICAgICAgICAgICAgImVucm9sbG1lbnRBY3Rpb25UeXBlIjogIkFERCIsDQogICAgICAgICAgICAiZW5yb2xsbWVudENhdGVnb3J5IjogIjEiLA0KICAgICAgICAgICAgIm9yZ2FuaXphdGlvbklkIjogIk9SRzMyNDIyIiwNCiAgICAgICAgICAgICJvcmdhbml6YXRpb25JbmZvIjogew0KICAgICAgICAgICAgICAgICJwYXJlbnRPcmdJZCI6ICJCMzIzMzIiLA0KICAgICAgICAgICAgICAgICJvcmdHcm91cHMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJvcmdHcm91cE5hbWUiOiAiTWlkZGxlTWFya2V0Ig0KICAgICAgICAgICAgICAgICAgICB9DQogICAgICAgICAgICAgICAgXSwNCiAgICAgICAgICAgICAgICAic3Vic2NyaWJlU2VydmljZXMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJzZXJ2aWNlQ29kZSI6ICIxIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAic2VydmljZUNvZGUiOiAiMyINCiAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgIF0sDQogICAgICAgICAgICAgICAgIm9yZ05hbWUiOiAiQVBQTEVDT1JQIiwNCiAgICAgICAgICAgICAgICAib3JnU2hvcnROYW1lIjogIkFQUExFIiwNCiAgICAgICAgICAgICAgICAidGF4SWQiOiAiOTk5MzMzMzMiLA0KICAgICAgICAgICAgICAgICJjb250YWN0RGV0YWlscyI6IFsNCiAgICAgICAgICAgICAgICAgICAgew0KICAgICAgICAgICAgICAgICAgICAgICAgImpvYlRpdGxlIjogIiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic2FsdXRhdGlvbiI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImZpcnN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1pZGRsZU5hbWUiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJsYXN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImVtYWlsSWQiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJwcmltYXJ5UGhvbmVObyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1vYmlsZVBob25lTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJleHRlbnNpb24iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJmYXhObyI6ICIiDQogICAgICAgICAgICAgICAgICAgIH0NCiAgICAgICAgICAgICAgICBdLA0KICAgICAgICAgICAgICAgICJvcmdBZGRyZXNzIjogew0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmUxIjogIjMyMzIgQkVMTCBSRCIsDQogICAgICAgICAgICAgICAgICAgICJhZGRyZXNzTGluZTIiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgImFkZHJlc3NMaW5lMyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmU0IjogIiIsDQogICAgICAgICAgICAgICAgICAgICJjaXR5IjogIlBIT0VOSVgiLA0KICAgICAgICAgICAgICAgICAgICAic3RhdGUiOiAiQVoiLA0KICAgICAgICAgICAgICAgICAgICAiY291bnRyeSI6ICJVU0EiLA0KICAgICAgICAgICAgICAgICAgICAiemlwQ29kZSI6ICI4NTAzMiINCiAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICJvcmdDaGVja0RldGFpbHMiOiB7DQogICAgICAgICAgICAgICAgICAgICJjaGVja1NldHRpbmdzIjogew0KICAgICAgICAgICAgICAgICAgICAgICAgImN1c3RGZWVCaWxsSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrTnVtYmVyaW5nSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrUHJpbnRTZXJ2aWNlSW5kIjogIlMiLA0KICAgICAgICAgICAgICAgICAgICAgICAgInVzZUZhc3RGb3J3YXJkU2VydmljZUluZCI6ICJGRiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic3VwcGxpZXJOb3RpZmljYXRpb25JbmQiOiAiIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICAiY2hlY2tEZWxpdmVyeUluZGljYXRvciI6IHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yTWV0aG9kQ29kZSI6ICJGRURFWCAyREFZIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJkZWxpdmVyeUluZGljYXRvciI6ICIxMDAwIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yQWNjTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJob2xkSW5kIjogIk4iDQogICAgICAgICAgICAgICAgICAgIH0sDQogICAgICAgICAgICAgICAgICAgICJwYXlvckF0dGFjaG1lbnQiOiBbDQogICAgICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF0dGFjaG1lbnRJZCI6ICJBVFRBQ0gxMjIxIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiYXR0YWNobWVudEZpbGUiOiAiIg0KICAgICAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgICAgICBdDQogICAgICAgICAgICAgICAgfSwNCiAgICAgICAgICAgICAgICAib3JnQWNoRGV0YWlscyI6IG51bGwsDQogICAgICAgICAgICAgICAgIm9yZ0NhcmREZXRhaWxzIjogbnVsbCwNCiAgICAgICAgICAgICAgICAib3JnSVdpcmVEZXRhaWxzIjogbnVsbA0KICAgICAgICAgICAgfQ0KICAgICAgICB9DQogICAgfQ0KfQ==\",\n \"signature1_indicator\": \"file1gif\",\n \"signature1_fileType\": null,\n \"signature2_image\": null,\n \"signature2_indicator\": null,\n \"signature2_fileType\": null\n }\n ],\n \"check_return_org_name\": \"Walmart\",\n \"check_return_address\": {\n \"type\": \"return\",\n \"address_line_1\": \"3232 Bell Rd\",\n \"address_line_2\": \"Suite 33224\",\n \"address_line_3\": null,\n \"address_line_4\": null,\n \"address_line_5\": null,\n \"city\": \"Phoenix\",\n \"region_code\": \"AZ\",\n \"region_name\": \"Arizona\",\n \"country_code\": \"US\",\n \"country_name\": \"USA\",\n \"zip_code\": \"85302\"\n }\n }\n ],\n \"risk_management_details\": {\n \"user_id\": \"NA23221\",\n \"title\": \"Executive\",\n \"salutation\": \"Mr\",\n \"first_name\": \"Michael\",\n \"middle_name\": null,\n \"last_name\": \"Johnson\",\n \"email_id\": \"Michael.Johnson@walmart.com\",\n \"phones\": [\n {\n \"number\": \"6025660123\",\n \"extension\": \"401\",\n \"type\": \"business\"\n }\n ],\n \"fax_no\": \"3434344565\",\n \"user_count\": \"50\",\n \"transaction_count\": \"44\",\n \"account_creation_date\": \"01/02/2015\",\n \"login_volume\": \"10\",\n \"user_tenure\": \"01/02/2015\",\n \"avg_num_of_fx_pymts\": \"50\",\n \"avg_fx_pymt_amt\": \"5000\",\n \"goods_and_services\": \"Purchasing from Suppliers\",\n \"ip_address\": \"10.235.11.11\",\n \"device_id\": \"D23232323\",\n \"session_id\": \"NE8822345\",\n \"submit_date\": \"01/02/2015 12:15:12\",\n \"user_agent\": {\n \"web_action_path\": \"Mozilla/5.0 (Windows NT 6.1) Gecko/20100101 Firefox/ 12.0 \",\n \"browser_type\": \"Firefox\",\n \"browser_version\": \"12.0\"\n }\n }\n }\n}";
        submitPostRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void updateAccountTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("UPDATE_ACCOUNT_RESOURCE");
        String method = "PUT";
        String payload = "{\n \"account_details\": {\n \"check_accts\": [\n {\n \"bank\": {\n \"name\": \"BankOfAmerica\",\n \"account_holder_name\": \"Michael\",\n \"account_number\": \"2354665565\",\n \"routing_number\": \"021000021\",\n \"acct_type\": \"chk\",\n \"address\": {\n \"type\": \"bank\",\n \"id\": \"ADB6VbQeM8yqaWyf\",\n \"address_line_1\": \"3232 Bell Rd\",\n \"address_line_2\": \"update testing\",\n \"address_line_3\": null,\n \"address_line_4\": null,\n \"address_line_5\": null,\n \"city\": \"Phoenix\",\n \"region_code\": \"AZ\",\n \"region_name\": \"Arizona\",\n \"country_code\": \"US\",\n \"country_name\": \"USA\",\n \"zip_code\": \"85302\"\n }\n },\n \"enable_threshold\": \"Y\",\n \"check_layout_indicator\": \"file1gif\",\n \"check_layout_image\": \"ew0KICAgICJtYW5hZ2VFbnJvbGxtZW50UmVxdWVzdCI6IHsNCiAgICAgICAgImRhdGEiOiB7DQogICAgICAgICAgICAiY29tbW9uUmVxdWVzdENvbnRleHQiOiB7DQogICAgICAgICAgICAgICAgInJlcXVlc3RJZCI6ICJBQTM0MzQzNDIzMjMiLA0KICAgICAgICAgICAgICAgICJwYXJ0bmVySWQiOiAiR1M2NDBCRzAwMSIsDQogICAgICAgICAgICAgICAgInBhcnRuZXJOYW1lIjogIklOVEFDQ1QiLA0KICAgICAgICAgICAgICAgICJ0aW1lc3RhbXAiOiAiMjAxMy0xMi0xM1QxMToxMDowMC43MTUtMDU6MDAiDQogICAgICAgICAgICB9LA0KICAgICAgICAgICAgImVucm9sbG1lbnRBY3Rpb25UeXBlIjogIkFERCIsDQogICAgICAgICAgICAiZW5yb2xsbWVudENhdGVnb3J5IjogIjEiLA0KICAgICAgICAgICAgIm9yZ2FuaXphdGlvbklkIjogIk9SRzMyNDIyIiwNCiAgICAgICAgICAgICJvcmdhbml6YXRpb25JbmZvIjogew0KICAgICAgICAgICAgICAgICJwYXJlbnRPcmdJZCI6ICJCMzIzMzIiLA0KICAgICAgICAgICAgICAgICJvcmdHcm91cHMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJvcmdHcm91cE5hbWUiOiAiTWlkZGxlTWFya2V0Ig0KICAgICAgICAgICAgICAgICAgICB9DQogICAgICAgICAgICAgICAgXSwNCiAgICAgICAgICAgICAgICAic3Vic2NyaWJlU2VydmljZXMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJzZXJ2aWNlQ29kZSI6ICIxIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAic2VydmljZUNvZGUiOiAiMyINCiAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgIF0sDQogICAgICAgICAgICAgICAgIm9yZ05hbWUiOiAiQVBQTEVDT1JQIiwNCiAgICAgICAgICAgICAgICAib3JnU2hvcnROYW1lIjogIkFQUExFIiwNCiAgICAgICAgICAgICAgICAidGF4SWQiOiAiOTk5MzMzMzMiLA0KICAgICAgICAgICAgICAgICJjb250YWN0RGV0YWlscyI6IFsNCiAgICAgICAgICAgICAgICAgICAgew0KICAgICAgICAgICAgICAgICAgICAgICAgImpvYlRpdGxlIjogIiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic2FsdXRhdGlvbiI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImZpcnN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1pZGRsZU5hbWUiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJsYXN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImVtYWlsSWQiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJwcmltYXJ5UGhvbmVObyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1vYmlsZVBob25lTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJleHRlbnNpb24iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJmYXhObyI6ICIiDQogICAgICAgICAgICAgICAgICAgIH0NCiAgICAgICAgICAgICAgICBdLA0KICAgICAgICAgICAgICAgICJvcmdBZGRyZXNzIjogew0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmUxIjogIjMyMzIgQkVMTCBSRCIsDQogICAgICAgICAgICAgICAgICAgICJhZGRyZXNzTGluZTIiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgImFkZHJlc3NMaW5lMyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmU0IjogIiIsDQogICAgICAgICAgICAgICAgICAgICJjaXR5IjogIlBIT0VOSVgiLA0KICAgICAgICAgICAgICAgICAgICAic3RhdGUiOiAiQVoiLA0KICAgICAgICAgICAgICAgICAgICAiY291bnRyeSI6ICJVU0EiLA0KICAgICAgICAgICAgICAgICAgICAiemlwQ29kZSI6ICI4NTAzMiINCiAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICJvcmdDaGVja0RldGFpbHMiOiB7DQogICAgICAgICAgICAgICAgICAgICJjaGVja1NldHRpbmdzIjogew0KICAgICAgICAgICAgICAgICAgICAgICAgImN1c3RGZWVCaWxsSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrTnVtYmVyaW5nSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrUHJpbnRTZXJ2aWNlSW5kIjogIlMiLA0KICAgICAgICAgICAgICAgICAgICAgICAgInVzZUZhc3RGb3J3YXJkU2VydmljZUluZCI6ICJGRiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic3VwcGxpZXJOb3RpZmljYXRpb25JbmQiOiAiIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICAiY2hlY2tEZWxpdmVyeUluZGljYXRvciI6IHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yTWV0aG9kQ29kZSI6ICJGRURFWCAyREFZIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJkZWxpdmVyeUluZGljYXRvciI6ICIxMDAwIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yQWNjTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJob2xkSW5kIjogIk4iDQogICAgICAgICAgICAgICAgICAgIH0sDQogICAgICAgICAgICAgICAgICAgICJwYXlvckF0dGFjaG1lbnQiOiBbDQogICAgICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF0dGFjaG1lbnRJZCI6ICJBVFRBQ0gxMjIxIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiYXR0YWNobWVudEZpbGUiOiAiIg0KICAgICAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgICAgICBdDQogICAgICAgICAgICAgICAgfSwNCiAgICAgICAgICAgICAgICAib3JnQWNoRGV0YWlscyI6IG51bGwsDQogICAgICAgICAgICAgICAgIm9yZ0NhcmREZXRhaWxzIjogbnVsbCwNCiAgICAgICAgICAgICAgICAib3JnSVdpcmVEZXRhaWxzIjogbnVsbA0KICAgICAgICAgICAgfQ0KICAgICAgICB9DQogICAgfQ0KfQ==\",\n \"check_layout_fileType\": null,\n \"logo_indicator\": \"file2gif\",\n \"logo_image\": \"ew0KICAgICJtYW5hZ2VFbnJvbGxtZW50UmVxdWVzdCI6IHsNCiAgICAgICAgImRhdGEiOiB7DQogICAgICAgICAgICAiY29tbW9uUmVxdWVzdENvbnRleHQiOiB7DQogICAgICAgICAgICAgICAgInJlcXVlc3RJZCI6ICJBQTM0MzQzNDIzMjMiLA0KICAgICAgICAgICAgICAgICJwYXJ0bmVySWQiOiAiR1M2NDBCRzAwMSIsDQogICAgICAgICAgICAgICAgInBhcnRuZXJOYW1lIjogIklOVEFDQ1QiLA0KICAgICAgICAgICAgICAgICJ0aW1lc3RhbXAiOiAiMjAxMy0xMi0xM1QxMToxMDowMC43MTUtMDU6MDAiDQogICAgICAgICAgICB9LA0KICAgICAgICAgICAgImVucm9sbG1lbnRBY3Rpb25UeXBlIjogIkFERCIsDQogICAgICAgICAgICAiZW5yb2xsbWVudENhdGVnb3J5IjogIjEiLA0KICAgICAgICAgICAgIm9yZ2FuaXphdGlvbklkIjogIk9SRzMyNDIyIiwNCiAgICAgICAgICAgICJvcmdhbml6YXRpb25JbmZvIjogew0KICAgICAgICAgICAgICAgICJwYXJlbnRPcmdJZCI6ICJCMzIzMzIiLA0KICAgICAgICAgICAgICAgICJvcmdHcm91cHMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJvcmdHcm91cE5hbWUiOiAiTWlkZGxlTWFya2V0Ig0KICAgICAgICAgICAgICAgICAgICB9DQogICAgICAgICAgICAgICAgXSwNCiAgICAgICAgICAgICAgICAic3Vic2NyaWJlU2VydmljZXMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJzZXJ2aWNlQ29kZSI6ICIxIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAic2VydmljZUNvZGUiOiAiMyINCiAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgIF0sDQogICAgICAgICAgICAgICAgIm9yZ05hbWUiOiAiQVBQTEVDT1JQIiwNCiAgICAgICAgICAgICAgICAib3JnU2hvcnROYW1lIjogIkFQUExFIiwNCiAgICAgICAgICAgICAgICAidGF4SWQiOiAiOTk5MzMzMzMiLA0KICAgICAgICAgICAgICAgICJjb250YWN0RGV0YWlscyI6IFsNCiAgICAgICAgICAgICAgICAgICAgew0KICAgICAgICAgICAgICAgICAgICAgICAgImpvYlRpdGxlIjogIiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic2FsdXRhdGlvbiI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImZpcnN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1pZGRsZU5hbWUiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJsYXN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImVtYWlsSWQiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJwcmltYXJ5UGhvbmVObyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1vYmlsZVBob25lTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJleHRlbnNpb24iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJmYXhObyI6ICIiDQogICAgICAgICAgICAgICAgICAgIH0NCiAgICAgICAgICAgICAgICBdLA0KICAgICAgICAgICAgICAgICJvcmdBZGRyZXNzIjogew0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmUxIjogIjMyMzIgQkVMTCBSRCIsDQogICAgICAgICAgICAgICAgICAgICJhZGRyZXNzTGluZTIiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgImFkZHJlc3NMaW5lMyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmU0IjogIiIsDQogICAgICAgICAgICAgICAgICAgICJjaXR5IjogIlBIT0VOSVgiLA0KICAgICAgICAgICAgICAgICAgICAic3RhdGUiOiAiQVoiLA0KICAgICAgICAgICAgICAgICAgICAiY291bnRyeSI6ICJVU0EiLA0KICAgICAgICAgICAgICAgICAgICAiemlwQ29kZSI6ICI4NTAzMiINCiAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICJvcmdDaGVja0RldGFpbHMiOiB7DQogICAgICAgICAgICAgICAgICAgICJjaGVja1NldHRpbmdzIjogew0KICAgICAgICAgICAgICAgICAgICAgICAgImN1c3RGZWVCaWxsSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrTnVtYmVyaW5nSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrUHJpbnRTZXJ2aWNlSW5kIjogIlMiLA0KICAgICAgICAgICAgICAgICAgICAgICAgInVzZUZhc3RGb3J3YXJkU2VydmljZUluZCI6ICJGRiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic3VwcGxpZXJOb3RpZmljYXRpb25JbmQiOiAiIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICAiY2hlY2tEZWxpdmVyeUluZGljYXRvciI6IHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yTWV0aG9kQ29kZSI6ICJGRURFWCAyREFZIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJkZWxpdmVyeUluZGljYXRvciI6ICIxMDAwIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yQWNjTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJob2xkSW5kIjogIk4iDQogICAgICAgICAgICAgICAgICAgIH0sDQogICAgICAgICAgICAgICAgICAgICJwYXlvckF0dGFjaG1lbnQiOiBbDQogICAgICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF0dGFjaG1lbnRJZCI6ICJBVFRBQ0gxMjIxIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiYXR0YWNobWVudEZpbGUiOiAiIg0KICAgICAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgICAgICBdDQogICAgICAgICAgICAgICAgfSwNCiAgICAgICAgICAgICAgICAib3JnQWNoRGV0YWlscyI6IG51bGwsDQogICAgICAgICAgICAgICAgIm9yZ0NhcmREZXRhaWxzIjogbnVsbCwNCiAgICAgICAgICAgICAgICAib3JnSVdpcmVEZXRhaWxzIjogbnVsbA0KICAgICAgICAgICAgfQ0KICAgICAgICB9DQogICAgfQ0KfQ==\",\n \"logo_fileType\": null,\n \"signature_details\": [\n {\n \"threshold_limit\": \"999999.90\",\n \"signature1_image\": \"ew0KICAgICJtYW5hZ2VFbnJvbGxtZW50UmVxdWVzdCI6IHsNCiAgICAgICAgImRhdGEiOiB7DQogICAgICAgICAgICAiY29tbW9uUmVxdWVzdENvbnRleHQiOiB7DQogICAgICAgICAgICAgICAgInJlcXVlc3RJZCI6ICJBQTM0MzQzNDIzMjMiLA0KICAgICAgICAgICAgICAgICJwYXJ0bmVySWQiOiAiR1M2NDBCRzAwMSIsDQogICAgICAgICAgICAgICAgInBhcnRuZXJOYW1lIjogIklOVEFDQ1QiLA0KICAgICAgICAgICAgICAgICJ0aW1lc3RhbXAiOiAiMjAxMy0xMi0xM1QxMToxMDowMC43MTUtMDU6MDAiDQogICAgICAgICAgICB9LA0KICAgICAgICAgICAgImVucm9sbG1lbnRBY3Rpb25UeXBlIjogIkFERCIsDQogICAgICAgICAgICAiZW5yb2xsbWVudENhdGVnb3J5IjogIjEiLA0KICAgICAgICAgICAgIm9yZ2FuaXphdGlvbklkIjogIk9SRzMyNDIyIiwNCiAgICAgICAgICAgICJvcmdhbml6YXRpb25JbmZvIjogew0KICAgICAgICAgICAgICAgICJwYXJlbnRPcmdJZCI6ICJCMzIzMzIiLA0KICAgICAgICAgICAgICAgICJvcmdHcm91cHMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJvcmdHcm91cE5hbWUiOiAiTWlkZGxlTWFya2V0Ig0KICAgICAgICAgICAgICAgICAgICB9DQogICAgICAgICAgICAgICAgXSwNCiAgICAgICAgICAgICAgICAic3Vic2NyaWJlU2VydmljZXMiOiBbDQogICAgICAgICAgICAgICAgICAgIHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJzZXJ2aWNlQ29kZSI6ICIxIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAic2VydmljZUNvZGUiOiAiMyINCiAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgIF0sDQogICAgICAgICAgICAgICAgIm9yZ05hbWUiOiAiQVBQTEVDT1JQIiwNCiAgICAgICAgICAgICAgICAib3JnU2hvcnROYW1lIjogIkFQUExFIiwNCiAgICAgICAgICAgICAgICAidGF4SWQiOiAiOTk5MzMzMzMiLA0KICAgICAgICAgICAgICAgICJjb250YWN0RGV0YWlscyI6IFsNCiAgICAgICAgICAgICAgICAgICAgew0KICAgICAgICAgICAgICAgICAgICAgICAgImpvYlRpdGxlIjogIiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic2FsdXRhdGlvbiI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImZpcnN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1pZGRsZU5hbWUiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJsYXN0TmFtZSI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImVtYWlsSWQiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJwcmltYXJ5UGhvbmVObyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAgICAgIm1vYmlsZVBob25lTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJleHRlbnNpb24iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJmYXhObyI6ICIiDQogICAgICAgICAgICAgICAgICAgIH0NCiAgICAgICAgICAgICAgICBdLA0KICAgICAgICAgICAgICAgICJvcmdBZGRyZXNzIjogew0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmUxIjogIjMyMzIgQkVMTCBSRCIsDQogICAgICAgICAgICAgICAgICAgICJhZGRyZXNzTGluZTIiOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgImFkZHJlc3NMaW5lMyI6ICIiLA0KICAgICAgICAgICAgICAgICAgICAiYWRkcmVzc0xpbmU0IjogIiIsDQogICAgICAgICAgICAgICAgICAgICJjaXR5IjogIlBIT0VOSVgiLA0KICAgICAgICAgICAgICAgICAgICAic3RhdGUiOiAiQVoiLA0KICAgICAgICAgICAgICAgICAgICAiY291bnRyeSI6ICJVU0EiLA0KICAgICAgICAgICAgICAgICAgICAiemlwQ29kZSI6ICI4NTAzMiINCiAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICJvcmdDaGVja0RldGFpbHMiOiB7DQogICAgICAgICAgICAgICAgICAgICJjaGVja1NldHRpbmdzIjogew0KICAgICAgICAgICAgICAgICAgICAgICAgImN1c3RGZWVCaWxsSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrTnVtYmVyaW5nSW5kIjogIlkiLA0KICAgICAgICAgICAgICAgICAgICAgICAgImNoZWNrUHJpbnRTZXJ2aWNlSW5kIjogIlMiLA0KICAgICAgICAgICAgICAgICAgICAgICAgInVzZUZhc3RGb3J3YXJkU2VydmljZUluZCI6ICJGRiIsDQogICAgICAgICAgICAgICAgICAgICAgICAic3VwcGxpZXJOb3RpZmljYXRpb25JbmQiOiAiIg0KICAgICAgICAgICAgICAgICAgICB9LA0KICAgICAgICAgICAgICAgICAgICAiY2hlY2tEZWxpdmVyeUluZGljYXRvciI6IHsNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yTWV0aG9kQ29kZSI6ICJGRURFWCAyREFZIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJkZWxpdmVyeUluZGljYXRvciI6ICIxMDAwIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJtYWlsVmVuZG9yQWNjTm8iOiAiIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICJob2xkSW5kIjogIk4iDQogICAgICAgICAgICAgICAgICAgIH0sDQogICAgICAgICAgICAgICAgICAgICJwYXlvckF0dGFjaG1lbnQiOiBbDQogICAgICAgICAgICAgICAgICAgICAgICB7DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgImF0dGFjaG1lbnRJZCI6ICJBVFRBQ0gxMjIxIiwNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAiYXR0YWNobWVudEZpbGUiOiAiIg0KICAgICAgICAgICAgICAgICAgICAgICAgfQ0KICAgICAgICAgICAgICAgICAgICBdDQogICAgICAgICAgICAgICAgfSwNCiAgICAgICAgICAgICAgICAib3JnQWNoRGV0YWlscyI6IG51bGwsDQogICAgICAgICAgICAgICAgIm9yZ0NhcmREZXRhaWxzIjogbnVsbCwNCiAgICAgICAgICAgICAgICAib3JnSVdpcmVEZXRhaWxzIjogbnVsbA0KICAgICAgICAgICAgfQ0KICAgICAgICB9DQogICAgfQ0KfQ==\",\n \"signature1_indicator\": \"file1gif\",\n \"signature1_fileType\": null,\n \"signature2_image\": null,\n \"signature2_indicator\": null,\n \"signature2_fileType\": null\n }\n ],\n \"check_return_org_name\": \"Walmart\",\n \"check_return_address\": {\n \"id\": \"ADB6VbQeM8yqaWyf\",\n \"type\": \"return\",\n \"address_line_1\": \"3232 Bell Rd\",\n \"address_line_2\": \"Suite 33224\",\n \"address_line_3\": null,\n \"address_line_4\": null,\n \"address_line_5\": null,\n \"city\": \"Phoenix\",\n \"region_code\": \"AZ\",\n \"region_name\": \"Arizona\",\n \"country_code\": \"US\",\n \"country_name\": \"USA\",\n \"zip_code\": \"85302\"\n }\n }\n ],\n \"risk_management_details\": {\n \"user_id\": \"NA23221\",\n \"title\": \"Executive\",\n \"salutation\": \"Mr\",\n \"first_name\": \"Michael\",\n \"middle_name\": null,\n \"last_name\": \"Johnson\",\n \"email_id\": \"Michael.Johnson@walmart.com\",\n \"phones\": [\n {\n \"number\": \"6025660123\",\n \"extension\": \"401\",\n \"type\": \"business\"\n }\n ],\n \"fax_no\": \"3434344565\",\n \"user_count\": \"50\",\n \"transaction_count\": \"44\",\n \"account_creation_date\": \"01/02/2015\",\n \"login_volume\": \"10\",\n \"user_tenure\": \"01/02/2015\",\n \"avg_num_of_fx_pymts\": \"50\",\n \"avg_fx_pymt_amt\": \"5000\",\n \"goods_and_services\": \"Purchasing from Suppliers\",\n \"ip_address\": \"10.235.11.11\",\n \"device_id\": \"D23232323\",\n \"session_id\": \"NE8822345\",\n \"submit_date\": \"01/02/2015 12:15:12\",\n \"user_agent\": {\n \"web_action_path\": \"Mozilla/5.0 (Windows NT 6.1) Gecko/20100101 Firefox/ 12.0 \",\n \"browser_type\": \"Firefox\",\n \"browser_version\": \"12.0\"\n }\n }\n }\n}";
        submitPutRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
    }
    
    @Ignore("This unit test requires a valid API key")
    @Test
    public void deleteAccountTest() throws IOException {
        AuthProvider authProvider = HmacAuthBuilder.getBuilder().setConfiguration(configurationProvider).build();
        String url = baseUrl + configurationProvider.getValue("DELETE_ACCOUNT_RESOURCE");
        String method = "DELETE";
        String payload = "";
        submitDeleteRequest(url, authProvider.generateAuthHeaders(payload, url, method), payload);
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
    
    /**
     * Builds and submits delete request. Unit test succeeds if status code is in [200, 300] range.
     * Amex requires the TLS 1.2, The test cases contained in this file employ TLS 1.2 please insure to retain the TLS 1.2 standard for any additions.
     * @param url Resource URL
     * @param headers Request headers
     * @param payload JSON payload
     * @throws IOException Exception
     */
    private void submitDeleteRequest(String url, Map<String, String> headers, String payload) throws IOException {
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .delete(RequestBody.create(JSON_MEDIA_TYPE, payload));

        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println(header.getKey() + " = " + header.getValue());
            builder.addHeader(header.getKey(), header.getValue());
        }
           
        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();

        System.out.print("Response: " + response.body().string());
        assertTrue(response.isSuccessful());
    }
    
    /**
     * Builds and submits put request. Unit test succeeds if status code is in [200, 300] range.
     * Amex requires the TLS 1.2, The test cases contained in this file employ TLS 1.2 please insure to retain the TLS 1.2 standard for any additions.
     * @param url Resource URL
     * @param headers Request headers
     * @param payload JSON payload
     * @throws IOException Exception
     */
    private void submitPutRequest(String url, Map<String, String> headers, String payload) throws IOException {
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .put(RequestBody.create(JSON_MEDIA_TYPE, payload));

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