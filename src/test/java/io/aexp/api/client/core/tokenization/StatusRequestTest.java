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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StatusRequestTest {

    @Test
    public void valid() {
        StatusRequest request = new StatusRequest.StatusRequestBuilder()
                .setTokenReferenceId("ref")
                .createStatusRequest();

        assertNotNull(request);
        //body is null for status request
        assertNull(request.toJson());
    }

    @Test
    public void getUri() {
        assertEquals("/payments/digital/v2/tokens/ref/status", new StatusRequest.StatusRequestBuilder()
                .setTokenReferenceId("ref").createStatusRequest().getUri());
    }

    @Test
    public void getHttpAction() {
        assertEquals("GET", new StatusRequest.StatusRequestBuilder().createStatusRequest().getHttpAction());
    }
}
