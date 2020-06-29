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

import io.aexp.api.client.core.models.ApiClientResponse;
import io.aexp.api.client.core.utils.EncryptionUtility;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ApiClientResponseTest {

    private final String ENCRYPTION_KEY = "YOUR ENCRYPTION KEY";

    @Test
    public void valid() {
        Header[] headers = {new Header() {
            public String getName() {
                return "session_id";
            }

            public String getValue() {
                return "12345";
            }

            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        }};
        ApiClientResponse response = new ApiClientResponse(headers, "{\"key\":\"value\"}");

        assertEquals("12345", response.getHeader("session_id"));
        assertEquals("{"+ System.lineSeparator() + "  \"key\" : \"value\""+ System.lineSeparator() + "}", response.toJson());
        assertEquals("value", response.getField("key"));
    }

    @Ignore("This test requires a valid key for decryption")
    @Test
    public void decryptField() {
        String value = "encrypt this";

        ApiClientResponse response = new ApiClientResponse(null, "{\"key\":\"" +
                EncryptionUtility.getInstance().encrypt(value, null, ENCRYPTION_KEY) + "\"}");

        assertEquals(value, response.getDecryptedField(ENCRYPTION_KEY, "key"));
    }

    @Ignore("This test requires a valid key for encryption")
    @Test
    public void decryptFieldInvalid() {
        String value = "encrypt this";

        ApiClientResponse response = new ApiClientResponse(null, "{\"obj\":{\"key\":\"" +
                EncryptionUtility.getInstance().encrypt(value, null, ENCRYPTION_KEY) + "\"}}");

        //Since obj is a json object, it cant decrypt, so it should return null
        assertNull(response.getDecryptedField(ENCRYPTION_KEY, "obj"));
    }


    @Test
    public void nulls() {
        ApiClientResponse response = new ApiClientResponse(null,null);

        assertEquals(null, response.getHeader("session_id"));
        assertEquals(null, response.toJson());
        assertEquals(null, response.getField("key"));
    }
}
