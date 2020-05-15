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

package io.aexp.api.client.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.aexp.api.client.core.exceptions.JsonException;

import java.util.Map;

public class JsonUtility {
    private final ObjectMapper mapper;
    private final static JsonUtility INSTANCE = new JsonUtility();

    private JsonUtility() {
        mapper = new ObjectMapper();

        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static JsonUtility getInstance() {
        return INSTANCE;
    }

    public <T> T getObject(String jsonString, Class<T> objectClass) {
        try {
            return mapper.readValue(jsonString, objectClass);
        } catch (Exception e) {
            throw new JsonException("Exception mapping string to class, caused by " + e.getMessage(), e);
        }
    }

    public String getString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonException("Exception writing object as string, caused by " + e.getMessage(), e);
        }
    }

    public String prettyString(String jsonString) {
        try {
            return getString(getObject(jsonString, Map.class));
        } catch(Exception e) {
            return jsonString;  //on error, just return the string passed in
        }
    }
}
