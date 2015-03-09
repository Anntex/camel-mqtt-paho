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

import com.anntex.camel.paho.config.PahoMqttConfiguration;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

/**
 * This class represents a MQTT Paho end point definition.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
@UriEndpoint(scheme = "mqtt", consumerClass = PahoMqttConsumer.class)
public class PahoMqttEndpoint extends DefaultEndpoint
{
    // --------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------

    /**
     * Defines a reference to a {@link PahoMqttConfiguration} configuration used
     * by this end point.
     */
    @UriParam
    private final PahoMqttConfiguration mConfiguration;

    // --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    /**
     * Constructor to create a new instance of a Eclipse MQTT Paho aware Camel
     * end point.
     *
     * @param uri
     *            The uri to connect the end point to a MQTT server. Default see
     *            {@link PahoMqttConfiguration#getHost()} .
     * @param component
     *            The component which creates and manages a Apache camel MQTT
     *            end point.
     * @param configuration
     *            The {@link PahoMqttConfiguration} configuration used by this
     *            end point definition.
     */
    public PahoMqttEndpoint(String uri, PahoMqttComponent component, PahoMqttConfiguration configuration)
    {
        super(uri, component);
        this.mConfiguration = configuration;
    }

    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    /**
     * @return the actual valid configuration definition.
     */
    public PahoMqttConfiguration getConfiguration()
    {
        return this.mConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Producer createProducer() throws Exception
    {
        return new PahoMqttProducer(this, this.mConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Consumer createConsumer(Processor processor) throws Exception
    {
        return new PahoMqttConsumer(this, processor, this.mConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
