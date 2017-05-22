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

import io.aexp.api.client.core.enums.NotificationType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class NotificationsRequestTest {

    @Test
    public void validDelete() {
        NotificationsRequest request = new NotificationsRequest.NotificationsRequestBuilder()
                .setTokenReferenceId("ref")
                .setNotificationType(NotificationType.DELETE)
                .createNotificationsRequest();

        assertNotNull(request);
        String json = request.toJson();
        assertTrue("", json.contains("\"notification_type\" : \"delete\""));
        assertTrue("", json.contains("\"token_ref_id\" : \"ref\""));
    }
    @Test
    public void validResume() {
        NotificationsRequest request = new NotificationsRequest.NotificationsRequestBuilder()
                .setTokenReferenceId("ref")
                .setNotificationType(NotificationType.RESUME)
                .createNotificationsRequest();

        assertNotNull(request);
        String json = request.toJson();
        assertTrue("", json.contains("\"notification_type\" : \"resume\""));
        assertTrue("", json.contains("\"token_ref_id\" : \"ref\""));
    }
    @Test
    public void validSuspend() {
        NotificationsRequest request = new NotificationsRequest.NotificationsRequestBuilder()
                .setTokenReferenceId("ref")
                .setNotificationType(NotificationType.SUSPEND)
                .createNotificationsRequest();

        assertNotNull(request);
        String json = request.toJson();
        assertTrue("", json.contains("\"notification_type\" : \"suspend\""));
        assertTrue("", json.contains("\"token_ref_id\" : \"ref\""));
    }



    @Test
    public void getUri() {
        assertEquals("/payments/digital/v2/tokens/notifications", new NotificationsRequest.NotificationsRequestBuilder().createNotificationsRequest().getUri());
    }

    @Test
    public void getHttpAction() {
        assertEquals("POST", new NotificationsRequest.NotificationsRequestBuilder().createNotificationsRequest().getHttpAction());
    }
}
