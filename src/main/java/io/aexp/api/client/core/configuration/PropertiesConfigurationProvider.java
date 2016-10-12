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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Provides an implementation of ConfigurationProvider which reads the configurations. 
 * from a property file. 
 */
public class PropertiesConfigurationProvider implements ConfigurationProvider {

    private Properties properties;

    public PropertiesConfigurationProvider() {
        properties = new Properties();
    }

   
    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties p){
    	this.properties = p;
    }
    

    @Override
    public String getValue(String key) {
        return (String)this.properties.get(key);
    }

    /**
     * Loads property details from the specified file path.
     * @throws IOException
     */
    public void loadProperties(String propertyFilePath) throws IOException {
        InputStream inputStream = new FileInputStream(propertyFilePath);
        loadProperties(inputStream);
    }

    /**
     * Loads property details from the specified input stream.
     * @throws IOException
     */
    public void loadProperties(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }
}
