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

package io.aexp.api.client.core.models;

import io.aexp.api.client.core.utils.EncryptionUtility;
import io.aexp.api.client.core.utils.JsonUtility;
import org.apache.http.Header;

import java.util.Map;

public class ApiClientResponse {
    private final Header[] headers;
    private final Map responseMap;
    private final String body;

    public ApiClientResponse(Header[] headers, String body) {
        this.headers = headers;

        if (body != null && !"".equals(body)) {
            body = JsonUtility.getInstance().prettyString(body);
        } else {
            body = null;
        }

        responseMap = body == null ? null : JsonUtility.getInstance().getObject(body, Map.class);

        this.body = body;
    }

    //Get a particular field from the body of the response
    public Object getField(String key) {
        if(responseMap != null) {
            return responseMap.get(key);
        }

        return null;
    }

    public String getHeader(String key) {
        if(headers != null) {
            for (Header header : headers) {
                if (key.equals(header.getName())) {
                    return header.getValue();
                }
            }
        }

        return null;
    }

    public String getDecryptedField(String keyStr, String field) {
        Object encryptedData = responseMap.get(field);

        if (encryptedData instanceof String) {
            return EncryptionUtility.getInstance().decrypt(keyStr, (String)encryptedData);
        }

        return null;
    }

    public String toJson() {
        return body;
    }
}
