/*
 * Copyright (c) 2016 American Express Travel Related Services Company, Inc.
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

package io.aexp.api.client.core.configuration;

import java.io.File;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class PropertiesConfigurationProviderTest {
	
    private ConfigurationProvider configurationProvider;
    private String propertiesFileName = "core.test.properties";
  
    
    
    @Before
    public void setup() throws IOException {
        PropertiesConfigurationProvider provider = new PropertiesConfigurationProvider();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(propertiesFileName).getFile());
        provider.loadProperties(file.getAbsolutePath());

        configurationProvider = provider;
    }

    @Test
    public void getValueSuccess() {
        String expected = "UNIT-TEST-KEY-4388-87b9-85cf463231d7";
        String actual = configurationProvider.getValue(ConfigurationKeys.CLIENT_KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void getValueFailure() {
        String expected = "Invalid property key";
        String actual = configurationProvider.getValue(ConfigurationKeys.CLIENT_KEY);
        assertNotEquals(expected, actual);
    }

    @Test
    public void getPropertiesSuccess() {
        assertNotNull(((PropertiesConfigurationProvider)configurationProvider).getProperties());
    }
    


    
}
