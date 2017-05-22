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
import io.aexp.api.client.core.models.ApiClientRequest;
import io.aexp.api.client.core.utils.JsonUtility;

import java.util.HashMap;
import java.util.Map;

public class NotificationsRequest extends ApiClientRequest {

    private final String tokenReferenceId;
    private final NotificationType notificationType;
    private final String NOTIFICATIONS_TARGET_URI = "/payments/digital/v2/tokens/notifications";

    private NotificationsRequest(String tokenReferenceId, NotificationType notificationType) {
        this.tokenReferenceId = tokenReferenceId;
        this.notificationType = notificationType;
    }

    public static class NotificationsRequestBuilder {

        private String tokenReferenceId;
        private NotificationType notificationType;

        public NotificationsRequestBuilder setTokenReferenceId(String tokenReferenceId) {
            this.tokenReferenceId = tokenReferenceId;
            return this;
        }

        public NotificationsRequestBuilder setNotificationType(NotificationType notificationType) {
            this.notificationType = notificationType;
            return this;
        }

        public NotificationsRequest createNotificationsRequest() {
            return new NotificationsRequest(tokenReferenceId, notificationType);
        }
    }

    @Override
    public String toJson(String kid, String aesKey) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("token_ref_id", tokenReferenceId);
        map.put("notification_type", notificationType.getType());

        return JsonUtility.getInstance().getString(map);
    }

    @Override
    public String getUri() {
        return NOTIFICATIONS_TARGET_URI;
    }

    @Override
    public String getHttpAction() {
        return "POST";
    }
}
