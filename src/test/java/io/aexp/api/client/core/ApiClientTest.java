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

package io.aexp.api.client.core;

import io.aexp.api.client.core.enums.AccountInputMethod;
import io.aexp.api.client.core.enums.EndPoint;
import io.aexp.api.client.core.exceptions.ExecutorException;
import io.aexp.api.client.core.models.ApiClientRequest;
import io.aexp.api.client.core.models.ApiClientResponse;
import io.aexp.api.client.core.security.authentication.AuthProvider;
import io.aexp.api.client.core.security.authentication.HmacAuthProvider;
import io.aexp.api.client.core.tokenization.ProvisioningRequest;
import io.aexp.api.client.core.tokenization.StatusRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiClientTest {

    private final String ENCRYPTION_KEY = "YOUR ENCRYPTION KEY";

    @Test
    public void validProduction() {
        ApiClient devPortalExecutor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.PRODUCTION)
                .setEncryptionInformation("ekid", "")
                .setSigningInformation("RS256", "kid", "")
                .setTokenRequesterId("token")
                .createDevPortalExecutor();

        assertNotNull(devPortalExecutor);
    }

    @Test
    public void validSandbox() {
        ApiClient devPortalExecutor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.SANDBOX)
                .setEncryptionInformation("ekid", "")
                .setSigningInformation("RS256", "kid", "")
                .setTokenRequesterId("token")
                .createDevPortalExecutor();

        assertNotNull(devPortalExecutor);
    }

    @Test(expected = ExecutorException.class)
    public void executeGet() throws Exception {

        ApiClient devPortalExecutor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.PRODUCTION)
                .setSigningInformation("RS256", "kid", "")
                .setTokenRequesterId("token")
                .createDevPortalExecutor();

        setTestOverride(devPortalExecutor, true);
        Field field = devPortalExecutor.getClass().getDeclaredField("httpClient");
        field.setAccessible(true);
        field.set(devPortalExecutor, HttpClientBuilder.create().build());
        AuthProvider authProvider = Mockito.mock(HmacAuthProvider.class);
        assertNotNull(devPortalExecutor.execute(
                new StatusRequest.StatusRequestBuilder().createStatusRequest(), authProvider));
    }

    private void setTestOverride(ApiClient executor, boolean override) throws Exception {
        Method method = executor.getClass().getDeclaredMethod("setTestOverride", boolean.class);
        method.setAccessible(true);

        method.invoke(executor, override);
    }

    @Ignore("This test requires a valid key for encryption")
    @Test(expected = ExecutorException.class)
    public void executePost() throws Exception {

        ProvisioningRequest provisioningRequest = new ProvisioningRequest.ProvisioningRequestBuilder()
                .setAccountNumber("371034317065001")
                .setExpiryMonth(12)
                .setExpiryYear(2030)
                .setUserEmailId("emailId")
                .setUserName("first|middle|last")
                .setUserPhoneNumber("1112224444")
                .setAccountInputMethod(AccountInputMethod.ON_FILE)
                .createProvisioningRequest();

        ApiClient devPortalExecutor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.PRODUCTION)
                .setSigningInformation("HS256", "", ENCRYPTION_KEY)
                .setTokenRequesterId("token")
                .createDevPortalExecutor();

        setTestOverride(devPortalExecutor, true);

        AuthProvider authProvider = Mockito.mock(HmacAuthProvider.class);

        assertNotNull(devPortalExecutor.execute(provisioningRequest, authProvider));
    }

    private Method getExecuteMethod(ApiClient executor) throws Exception {
        Method method = executor.getClass()
                .getDeclaredMethod("execute", HttpHost.class, ApiClientRequest.class, AuthProvider.class);
        method.setAccessible(true);

        return method;
    }

    @Test
    public void execute500Response() throws Exception {
        ApiClient executor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.PRODUCTION)
                .createDevPortalExecutor();

        Method method = getExecuteMethod(executor);

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);

        Field field = executor.getClass().getDeclaredField("httpClient");
        field.setAccessible(true);
        field.set(executor,httpClient);
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(response.getStatusLine()).thenReturn(statusLine);
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{\"k\":\"v\"}".getBytes()));
        when(response.getEntity()).thenReturn(entity);
        when(httpClient.execute((HttpHost) anyObject(), (HttpRequest) anyObject())).thenReturn(response);
        AuthProvider authProvider = Mockito.mock(HmacAuthProvider.class);
        HttpHost httpHost = new HttpHost("apigateway.americanexpress.com", 443, "https");
        Throwable exception = null;
        try {
            method.invoke(executor, httpHost,
                    new StatusRequest.StatusRequestBuilder().createStatusRequest(), authProvider);
        } catch (InvocationTargetException e) {
            exception = e.getCause();
        }

        assertNotNull(exception);
        assertTrue(exception instanceof ExecutorException);
    }

    @Test
    public void nullResponse() throws Exception {
        ApiClient executor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.PRODUCTION)
                .createDevPortalExecutor();

        Method method = getExecuteMethod(executor);

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        Field field = executor.getClass().getDeclaredField("httpClient");
        field.setAccessible(true);
        field.set(executor,httpClient);
        when(httpClient.execute((HttpHost) anyObject(), (HttpRequest) anyObject())).thenReturn(null);
        AuthProvider authProvider = Mockito.mock(HmacAuthProvider.class);
        HttpHost httpHost = new HttpHost("apigateway.americanexpress.com", 443, "https");
        ApiClientResponse response = (ApiClientResponse) method.invoke(executor, httpHost,
                new StatusRequest.StatusRequestBuilder().createStatusRequest(), authProvider);

        assertNotNull(response);
        assertNull(response.toJson());
    }

    @Test
    public void execute() throws Exception {
        ApiClient executor = new ApiClient.DevPortalExecutorBuilder()
                .setEndpoint(EndPoint.PRODUCTION)
                .createDevPortalExecutor();

        Method method = getExecuteMethod(executor);

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        Field field = executor.getClass().getDeclaredField("httpClient");
        field.setAccessible(true);
        field.set(executor,httpClient);
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        HttpEntity entity = mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{\"key\":\"v\"}".getBytes()));
        when(response.getEntity()).thenReturn(entity);
        when(httpClient.execute((HttpHost) anyObject(), (HttpRequest) anyObject())).thenReturn(response);
        AuthProvider authProvider = Mockito.mock(HmacAuthProvider.class);
        HttpHost httpHost = new HttpHost("apigateway.americanexpress.com", 443, "https");
        ApiClientResponse devResponse = (ApiClientResponse) method.invoke(executor, httpHost,
                new StatusRequest.StatusRequestBuilder().createStatusRequest(), authProvider);

        assertNotNull(devResponse);
        assertEquals("v", devResponse.getField("key"));
    }
}
