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

import io.aexp.api.client.core.enums.EndPoint;
import io.aexp.api.client.core.exceptions.ExecutorException;
import io.aexp.api.client.core.models.ApiClientRequest;
import io.aexp.api.client.core.models.ApiClientResponse;
import io.aexp.api.client.core.security.authentication.AuthHeaderNames;
import io.aexp.api.client.core.security.authentication.AuthProvider;
import io.aexp.api.client.core.utils.EncryptionUtility;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private final EndPoint endpoint;
    private final String tokenRequesterId;
    private final String encryptionKeyId;
    private final String encryptionKeyStr;
    private final String signingAlgorithm;
    private final String signingKeyId;
    private final String signingKeyStr;
    private final Integer timeout;

    private boolean testOverride;

    private static CloseableHttpClient httpClient;
    private final Integer DEFAULT_TIMEOUT = 15000;

    public ApiClient(EndPoint endpoint, String signingAlgorithm, String tokenRequesterId,
                     String encryptionKeyId, String encryptionKeyStr, String signingKeyId, String signingKeyStr,
                     Integer timeout) {
        this.endpoint = endpoint;
        this.signingAlgorithm = signingAlgorithm;
        this.tokenRequesterId = tokenRequesterId;
        this.encryptionKeyId = encryptionKeyId;
        this.encryptionKeyStr = encryptionKeyStr;
        this.signingKeyId = signingKeyId;
        this.signingKeyStr = signingKeyStr;
        this.timeout = timeout;
        setTestOverride(false);
    }

    public static class DevPortalExecutorBuilder {
        private EndPoint endpoint;
        private String tokenRequesterId;
        private String encryptionKeyId;
        private String encryptionKeyStr;
        private String signingAlgorithm;
        private String signingKeyId;
        private String signingKeyStr;
        private Integer timeout;

        public DevPortalExecutorBuilder setEndpoint(EndPoint endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public DevPortalExecutorBuilder setSigningInformation(String algorithm, String keyId, String keyStr) {
            this.signingAlgorithm = algorithm;
            this.signingKeyId = keyId;
            this.signingKeyStr = keyStr;
            return this;
        }

        public DevPortalExecutorBuilder setTokenRequesterId(String tokenRequesterId) {
            this.tokenRequesterId = tokenRequesterId;
            return this;
        }

        public DevPortalExecutorBuilder setEncryptionInformation(String keyId, String keyStr) {
            this.encryptionKeyId = keyId;
            this.encryptionKeyStr = keyStr;
            return this;
        }

        public DevPortalExecutorBuilder setTimeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        public ApiClient createDevPortalExecutor() {
            return new ApiClient(endpoint, signingAlgorithm, tokenRequesterId, encryptionKeyId,
                    encryptionKeyStr, signingKeyId, signingKeyStr, timeout);
        }
    }

    //This is here just for overriding the http client for testing
    private void setTestOverride(boolean override) {
        testOverride = override;
    }

    //Returns the proper http request based on the dev portal request
    private HttpRequestBase getHttpRequest(HttpHost httpHost, ApiClientRequest request, AuthProvider authProvider)
            throws UnsupportedEncodingException {
        String httpAction = request.getHttpAction();
        String payload = null;
        String targetUrl = httpHost.toURI().toString() + request.getUri().toString();
        if (httpAction.equals("POST")) {

            HttpPost httpPost = new HttpPost(request.getUri());
            if (signingAlgorithm != null && signingKeyId != null && signingKeyStr != null && encryptionKeyId != null &&
                    encryptionKeyStr != null) {
                payload = EncryptionUtility.getInstance().sign(signingAlgorithm, signingKeyId, signingKeyStr,
                        request.toJson(encryptionKeyId, encryptionKeyStr));
            } else {
                payload = request.toJson();
            }

            if (payload != null) {
                StringEntity sentity = new StringEntity(payload);

                sentity.setContentType("application/json");

                httpPost.setEntity(sentity);
            }

            Map<String, String> headers = authProvider.generateAuthHeaders(payload, targetUrl, request.getHttpAction());
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.addHeader(header.getKey(), header.getValue());
            }
            return httpPost;

        } else {
            HttpGet httpGet = new HttpGet(request.getUri());
            Map<String, String> headers = authProvider.generateAuthHeaders(payload, targetUrl, request.getHttpAction());
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpGet.addHeader(header.getKey(), header.getValue());
            }
            return httpGet;
        }
    }

    public ApiClientResponse execute(ApiClientRequest request, AuthProvider authProvider) {

        HttpHost httpHost = new HttpHost(endpoint.getHostname(), endpoint.getPort(), endpoint.getScheme());

        if (testOverride) {  //To make unit tests fail correctly during testing.  Otherwise they hang trying to connect.
            httpHost = new HttpHost("localhost", 55555, "http");
        }
        return execute(httpHost, request, authProvider);

    }

    private ApiClientResponse execute(HttpHost httpHost, ApiClientRequest request, AuthProvider authProvider) {
        try {

            if (httpClient == null) {
                httpClient = createClient();
            }
            HttpRequestBase httpRequest = getHttpRequest(httpHost, request, authProvider);

            if (tokenRequesterId != null && !"".equalsIgnoreCase(tokenRequesterId)) {
                httpRequest.addHeader(AuthHeaderNames.X_AMEX_TOKENREQUESTER_ID, tokenRequesterId);
            }

            HttpResponse response = httpClient.execute(httpHost, httpRequest);

            if (response != null && response.getStatusLine().getStatusCode() != 200) {
                throw new ExecutorException("Received http status from server: " +
                        response.getStatusLine().getStatusCode());
            }

            if (response == null) {
                return new ApiClientResponse(null, null);

            } else {
                return new ApiClientResponse(response.getAllHeaders(), EntityUtils.toString(response.getEntity()));

            }

        } catch (Exception e) {
            throw new ExecutorException("Exception sending request to server: " + e.getMessage(), e);
        }
    }

    private CloseableHttpClient createClient() {

        try {

            final SSLContext sslcontext = SSLContexts.custom().useProtocol("TLSv1.2").build();
            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {

                @Override
                public Socket createLayeredSocket(
                        final Socket socket,
                        final String target,
                        final int port,
                        final HttpContext context) throws IOException {
                    context.setAttribute("__enable_sni__", true);
                    return super.createLayeredSocket(socket, target, port, context);
                }
            };

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionFactory)
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .build();
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                    new PoolingHttpClientConnectionManager(registry);
            poolingHttpClientConnectionManager.closeExpiredConnections();

            poolingHttpClientConnectionManager
                    .closeIdleConnections((timeout != null) ? timeout : DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            RequestConfig requestConfig = RequestConfig
                    .custom().setConnectTimeout((timeout != null) ? timeout : DEFAULT_TIMEOUT)
                    .setConnectionRequestTimeout((timeout != null) ? timeout : DEFAULT_TIMEOUT)
                    .setSocketTimeout((timeout != null) ? timeout : DEFAULT_TIMEOUT)
                    .build();
            httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
        } catch (Exception ex) {
            httpClient = HttpClients.createDefault();
        }

        return httpClient;
    }
}
