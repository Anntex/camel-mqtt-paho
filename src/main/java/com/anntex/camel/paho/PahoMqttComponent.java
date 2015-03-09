/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anntex.camel.paho;

import java.util.Map;

import com.anntex.camel.paho.config.PahoMqttConfiguration;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * This class represents a component that will be used by Apache Camel to create and manage a {@link PahoMqttEndpoint}.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttComponent extends UriEndpointComponent
{
    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    public PahoMqttComponent()
    {
        super(PahoMqttEndpoint.class);
    }

    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception
    {
        PahoMqttConfiguration configuration = new PahoMqttConfiguration();

        // set the properties read from configuration and override these properties given as parameters
        this.setProperties(configuration, parameters);

        // create a new end point
        PahoMqttEndpoint endpoint = new PahoMqttEndpoint(uri, this, configuration);

        return endpoint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean useRawUri()
    {
        return true;
    }
}