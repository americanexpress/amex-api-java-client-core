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
import io.aexp.api.client.core.models.ApiClientRequest;
import io.aexp.api.client.core.utils.EncryptionUtility;
import io.aexp.api.client.core.utils.JsonUtility;

import java.util.HashMap;
import java.util.Map;

public class ProvisioningRequest extends ApiClientRequest {

    private final String postalCode;
    private final String accountNumber;
    private final String userPhoneNumber;
    private final String userEmailId;
    private final String userName;
    private final Integer expiryMonth;
    private final Integer expiryYear;
    private final AccountInputMethod accountInputMethod;
    private final String addressLine1;
    private final String addressLine2;
    private final String addressLine3;
    private final String city;
    private final String state;
    private final String country;
    private final String ipAddress;
    private final String PROVISIONING_TARGET_URI = "/payments/digital/v2/tokens/provisioning";

    public ProvisioningRequest(String postalCode, String accountNumber, String userPhoneNumber, String userEmailId,
                               String userName, Integer expiryMonth, Integer expiryYear,
                               AccountInputMethod accountInputMethod, String addressLine1, String addressLine2,
                               String addressLine3, String city, String state, String country, String ipAddress) {
        this.postalCode = postalCode;
        this.accountNumber = accountNumber;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmailId = userEmailId;
        this.userName = userName;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.accountInputMethod = accountInputMethod;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.ipAddress = ipAddress;

    }

    public static class ProvisioningRequestBuilder {

        private String postalCode;
        private String accountNumber;
        private String userPhoneNumber;
        private String userEmailId;
        private String userName;
        private Integer expiryMonth;
        private Integer expiryYear;
        private AccountInputMethod accountInputMethod;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private String city;
        private String state;
        private String country;
        private String ipAddress;

        public ProvisioningRequestBuilder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public ProvisioningRequestBuilder setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public ProvisioningRequestBuilder setUserPhoneNumber(String userPhoneNumber) {
            this.userPhoneNumber = userPhoneNumber;
            return this;
        }

        public ProvisioningRequestBuilder setUserEmailId(String userEmailId) {
            this.userEmailId = userEmailId;
            return this;
        }

        public ProvisioningRequestBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public ProvisioningRequestBuilder setExpiryMonth(Integer expiryMonth) {
            this.expiryMonth = expiryMonth;
            return this;
        }

        public ProvisioningRequestBuilder setExpiryYear(Integer expiryYear) {
            this.expiryYear = expiryYear;
            return this;
        }

        public ProvisioningRequestBuilder setAccountInputMethod(AccountInputMethod accountInputMethod) {
            this.accountInputMethod = accountInputMethod;
            return this;
        }

        public ProvisioningRequestBuilder setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public ProvisioningRequestBuilder setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public ProvisioningRequestBuilder setAddressLine3(String addressLine3) {
            this.addressLine3 = addressLine3;
            return this;
        }

        public ProvisioningRequestBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public ProvisioningRequestBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public ProvisioningRequestBuilder setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public ProvisioningRequestBuilder setState(String state) {
            this.state = state;
            return this;
        }

        public ProvisioningRequest createProvisioningRequest() {
            return new ProvisioningRequest(postalCode, accountNumber, userPhoneNumber, userEmailId, userName,
                    expiryMonth, expiryYear, accountInputMethod, addressLine1, addressLine2, addressLine3, city, state,
                    country, ipAddress);
        }
    }

    @Override
    public String toJson(String kid, String aesKey) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("user_data", getUserData());
        map.put("risk_assessment_data", getRiskData());
        map.put("account_data", aesKey == null ? "Need key for encryption" :
                EncryptionUtility.getInstance().encrypt(getAccountData(), kid, aesKey));

        return JsonUtility.getInstance().getString(map);
    }

    private String getAccountData() {
        Map<String, Object> accountMap = new HashMap<String, Object>();
        Map<String, Object> accountDataMap = new HashMap<String, Object>();

        String accountType = "credit_card";

        accountMap.put("account_type", accountType);
        accountMap.put("credit_card", accountDataMap);
        accountMap.put("billing_address", getBillingAddress());

        accountDataMap.put("account_number", accountNumber);
        accountDataMap.put("expiry_month", expiryMonth);
        accountDataMap.put("expiry_year", expiryYear);

        return JsonUtility.getInstance().getString(accountMap);
    }

    private Object getUserData() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("phone", userPhoneNumber);
        map.put("email", userEmailId);
        map.put("name", userName);

        return map;
    }

    private Object getBillingAddress() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("address_line1", addressLine1);
        map.put("address_line2", addressLine2);
        map.put("address_line3", addressLine3);
        map.put("city", city);
        map.put("state", state);
        map.put("postal_code", postalCode);
        map.put("country", country);

        return map;
    }


    private Object getRiskData() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("account_input_method", accountInputMethod == null ? null : accountInputMethod.getValue());
        map.put("ip_address", ipAddress);

        return map;
    }

    @Override
    public String getUri() {
        return PROVISIONING_TARGET_URI;
    }

    @Override
    public String getHttpAction() {
        return "POST";
    }
}
