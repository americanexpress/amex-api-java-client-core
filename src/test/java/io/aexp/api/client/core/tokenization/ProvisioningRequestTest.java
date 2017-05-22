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

package io.aexp.api.client.core.tokenization;

import io.aexp.api.client.core.enums.AccountInputMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProvisioningRequestTest {

    @Test
    public void validUserInput() {
        ProvisioningRequest request = new ProvisioningRequest.ProvisioningRequestBuilder()
                .setAccountInputMethod(AccountInputMethod.USER_INPUT)
                .setAccountNumber("371111111111111")
                .setExpiryMonth(2)
                .setExpiryYear(2020)
                .setUserEmailId("emailId")
                .setUserName("first|middle|last")
                .setUserPhoneNumber("1112224444")
                .setAddressLine1("address1")
                .setAddressLine2("address2")
                .setAddressLine3("address3")
                .setCity("city")
                .setState("state")
                .setCountry("country")
                .setIpAddress("ipaddress")
                .setPostalCode("11111")
                .createProvisioningRequest();

        assertNotNull(request);
        String json = request.toJson("kid", "LvXdoSVmSc0VxTRd13DfNYS7qKEVdsHURcBI/FSnv2w=");
        assertTrue(json.contains("\"risk_assessment_data\"") && json.contains("\"account_input_method\"") && json.contains("\"User Input\""));
        assertTrue(json.contains("\"account_data\" :"));
        assertTrue(json.contains("\"user_data\" : {"));
        assertTrue(json.contains("\"phone\" : \"1112224444\""));
        assertTrue(json.contains("\"email\" : \"emailId\""));
        assertTrue(json.contains("\"name\" : \"first|middle|last\""));

    }

    @Test
    public void validOnFile() {
        ProvisioningRequest request = new ProvisioningRequest.ProvisioningRequestBuilder()
                .setAccountInputMethod(AccountInputMethod.ON_FILE)
                .setAccountNumber("371111111111111")
                .setExpiryMonth(2)
                .setExpiryYear(2020)
                .setUserEmailId("emailId")
                .setUserName("first|middle|last")
                .setUserPhoneNumber("1112224444")
                .setAddressLine1("address1")
                .setAddressLine2("address2")
                .setAddressLine3("address3")
                .setCity("city")
                .setState("state")
                .setCountry("country")
                .setIpAddress("ipaddress")
                .setPostalCode("11111")
                .createProvisioningRequest();

        assertNotNull(request);
        String json = request.toJson("kid", "LvXdoSVmSc0VxTRd13DfNYS7qKEVdsHURcBI/FSnv2w=");
        assertTrue(json.contains("\"risk_assessment_data\"") && json.contains("\"account_input_method\"") && json.contains("\"On File\""));
        assertTrue(json.contains("\"account_data\" :"));
        assertTrue(json.contains("\"user_data\" : {"));
        assertTrue(json.contains("\"phone\" : \"1112224444\""));
        assertTrue(json.contains("\"email\" : \"emailId\""));
        assertTrue(json.contains("\"name\" : \"first|middle|last\""));
    }

    @Test
    public void validNone() {
        ProvisioningRequest request = new ProvisioningRequest.ProvisioningRequestBuilder()
                .setAccountNumber("371111111111111")
                .setExpiryMonth(2)
                .setExpiryYear(2020)
                .setUserEmailId("emailId")
                .setUserName("first|middle|last")
                .setUserPhoneNumber("1112224444")
                .setAddressLine1("address1")
                .setAddressLine2("address2")
                .setAddressLine3("address3")
                .setCity("city")
                .setState("state")
                .setCountry("country")
                .setIpAddress("ipaddress")
                .setPostalCode("11111")
                .createProvisioningRequest();

        assertNotNull(request);
        String json = request.toJson("kid", null);

        System.out.println(json);

        assertTrue(json.contains("\"account_data\" : \"Need key for encryption\""));
        assertTrue(json.contains("\"user_data\" : {"));
        assertTrue(json.contains("\"phone\" : \"1112224444\""));
        assertTrue(json.contains("\"email\" : \"emailId\""));
        assertTrue(json.contains("\"name\" : \"first|middle|last\""));
    }


    @Test
    public void getUri() {
        assertEquals("/payments/digital/v2/tokens/provisioning", new ProvisioningRequest.ProvisioningRequestBuilder().createProvisioningRequest().getUri());
    }

    @Test
    public void getHttpAction() {
        assertEquals("POST", new ProvisioningRequest.ProvisioningRequestBuilder().createProvisioningRequest().getHttpAction());
    }
}
