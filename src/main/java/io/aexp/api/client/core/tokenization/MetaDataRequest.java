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

import io.aexp.api.client.core.models.ApiClientRequest;

public class MetaDataRequest extends ApiClientRequest {

    private final String tokenReferenceId;
    private final String METADATA_TARGET_URI = "/payments/digital/v2/tokens/%s/metadata";

    private MetaDataRequest(String tokenReferenceId) {
        this.tokenReferenceId = tokenReferenceId;
    }

    public static class MetaDataRequestBuilder {

        private String tokenReferenceId;

        public MetaDataRequestBuilder setTokenReferenceId(String tokenReferenceId) {
            this.tokenReferenceId = tokenReferenceId;
            return this;
        }

        public MetaDataRequest createMetaDataRequest() {
            return new MetaDataRequest(tokenReferenceId);
        }
    }

    @Override
    public String toJson(String kid, String aesKey) {
        return null;
    }

    @Override
    public String getUri() {
        return String.format(METADATA_TARGET_URI, tokenReferenceId);
    }

    @Override
    public String getHttpAction() {
        return "GET";
    }
}
