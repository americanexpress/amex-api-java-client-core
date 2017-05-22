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

import io.aexp.api.client.core.exceptions.JsonException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonUtilityTest {
    static private JsonUtility jsonUtility = JsonUtility.getInstance();

    @Test
    public void getString() {
        Map<String,String>map = new HashMap<String, String>();

        assertEquals("{ }", jsonUtility.getString(map));
    }

    @Test(expected = JsonException.class)
    public void getStringNull() {
        jsonUtility.getString(new ImmutableObject("value"));
    }

    @Test
    public void getObject() {
        Map<String,String>map = jsonUtility.getObject("{ }", Map.class);

        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test(expected= JsonException.class)
    public void getObjectNull() {
        jsonUtility.getObject("{\" }", Map.class);
    }

    @Test
    public void prettyString() {
        String pretty = jsonUtility.prettyString("{\"key\":\"value\" }");

        assertNotNull(pretty);
        assertEquals("{\n  \"key\" : \"value\"\n}", pretty);
    }
    @Test
    public void prettyStringInvalid() {
        String pretty = jsonUtility.prettyString("{\"}");

        assertNotNull(pretty);
        assertEquals("{\"}", pretty);
    }

    private class ImmutableObject {
        private final String value;

        public ImmutableObject(String value) {
            this.value = value;
        }
    }
}
