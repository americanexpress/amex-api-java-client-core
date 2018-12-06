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

package tokenization;

import io.aexp.api.client.core.ApiClient;
import io.aexp.api.client.core.configuration.PropertiesConfigurationProvider;
import io.aexp.api.client.core.enums.AccountInputMethod;
import io.aexp.api.client.core.enums.EndPoint;
import io.aexp.api.client.core.enums.NotificationType;
import io.aexp.api.client.core.models.ApiClientResponse;
import io.aexp.api.client.core.security.authentication.AuthProvider;
import io.aexp.api.client.core.security.authentication.HmacAuthBuilder;
import io.aexp.api.client.core.tokenization.MetaDataRequest;
import io.aexp.api.client.core.tokenization.NotificationsRequest;
import io.aexp.api.client.core.tokenization.ProvisioningRequest;
import io.aexp.api.client.core.tokenization.StatusRequest;

import java.util.Properties;

public class AmexTokenizationClient {

    private static final String CLIENT_ID = "YOUR CLIENT ID";
    private static final String CLIENT_SECRET = "YOUR CLIENT SECRET";
    private static final String ENCRYPTION_KEY = "YOUR ENCRYPTION KEY";
    private static final String ENCRYPTION_KID = "YOUR ENCRYPTION KEY ID";
    private static final String TOKEN_REQUESTER_ID = "YOUR ASSIGNED TOKEN REQUESTER ID";

    public ApiClientResponse provisioning(String accountNumber, int expiryMonth, int expiryYear, String userEmailId,
                                          String userName, String userPhone, String postalCode) {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("CLIENT_SECRET", CLIENT_SECRET);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);


        ApiClient client = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.QA)
                .setTokenRequesterId(TOKEN_REQUESTER_ID)
                .setEncryptionInformation(ENCRYPTION_KID, ENCRYPTION_KEY)
                .setTimeout(15000)
                .createDevPortalExecutor();


        ProvisioningRequest provisioningRequest = new ProvisioningRequest.ProvisioningRequestBuilder()
                .setAccountNumber(accountNumber)
                .setExpiryMonth(expiryMonth)
                .setExpiryYear(expiryYear)
                .setUserEmailId(userEmailId)
                .setUserName(userName)
                .setUserPhoneNumber(userPhone)
                .setPostalCode(postalCode)
                .setAccountInputMethod(AccountInputMethod.ON_FILE)
                .createProvisioningRequest();

        // This generates the AMEX specific authentication headers needed for this API.
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        return client.execute(provisioningRequest, authProvider);
    }

    public ApiClientResponse notifications(String tokenReferenceId, NotificationType notificationType) {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("CLIENT_SECRET", CLIENT_SECRET);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        ApiClient client = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.QA)
                .setTokenRequesterId(TOKEN_REQUESTER_ID)
                .setTimeout(15000)
                .createDevPortalExecutor();

        NotificationsRequest notificationsRequest = new NotificationsRequest.NotificationsRequestBuilder()
                .setTokenReferenceId(tokenReferenceId)
                .setNotificationType(notificationType)
                .createNotificationsRequest();

        // This generates the AMEX specific authentication headers needed for this API.
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        return client.execute(notificationsRequest, authProvider);
    }

    public ApiClientResponse status(String tokenReferenceId) {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("CLIENT_SECRET", CLIENT_SECRET);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        ApiClient client = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.QA)
                .setTokenRequesterId(TOKEN_REQUESTER_ID)
                .setTimeout(15000)
                .createDevPortalExecutor();

        StatusRequest statusRequest = new StatusRequest.StatusRequestBuilder()
                .setTokenReferenceId(tokenReferenceId)
                .createStatusRequest();

        // This generates the AMEX specific authentication headers needed for this API.
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        return client.execute(statusRequest, authProvider);
    }

    public ApiClientResponse metadata(String tokenReferenceId) {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("CLIENT_SECRET", CLIENT_SECRET);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        ApiClient client = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.QA)
                .setTokenRequesterId(TOKEN_REQUESTER_ID)
                .createDevPortalExecutor();

        MetaDataRequest metaDataRequest = new MetaDataRequest.MetaDataRequestBuilder()
                .setTokenReferenceId(tokenReferenceId)
                .createMetaDataRequest();

        // This generates the AMEX specific authentication headers needed for this API.
        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        return client.execute(metaDataRequest, authProvider);
    }

  public static void main(String args[]) {
    AmexTokenizationClient client = new AmexTokenizationClient();

    ApiClientResponse response = client.provisioning("371111111111111", 12, 2020,"nobody@netcetera.com",
      "first|middle|last", "+0011112224444", "11111");
    System.out.println("Answer=" + response.toJson());
    System.out.println("TokenData=" + response.getDecryptedField(ENCRYPTION_KEY, "secure_token_data"));
  }
////
//  public static void main(String args[]) {
//    AmexTokenizationClient client = new AmexTokenizationClient();
//    System.out.println(client.metadata("379970315676848").toJson());
//  }


//    public static void main(String args[]) {
//      AmexTokenizationClient client = new AmexTokenizationClient();
//      System.out.println(client.status("124123").toJson());
//    }


    /*  public static void main(String args[]) {
        AmexTokenizationClient client = new AmexTokenizationClient();
        System.out.println(client.notifications("4444", NotificationType.RESUME).toJson());
      }*/
}
