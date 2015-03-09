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
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.NotActiveException;

/**
 * The producer class is responsible to process content and transform
 * this into MQTT messages to be able to publish these information to a MQTT
 * broker instance. Publishing information is realized by calling the Eclipse
 * Paho MQTT client implementation of the component.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttProducer extends DefaultProducer
{
    // --------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------

    private static final Logger         LOG = LoggerFactory.getLogger(PahoMqttProducer.class);

    /**
     * Defines a MQTT client based on the Paho implementation.
     */
    private final MqttClient            mMqttClient;

    /**
     * Defines the MQTT connection options for the end point client.
     */
    private final MqttConnectOptions    mConnectOptions;

    /**
     * Defines a reference to a {@link PahoMqttConfiguration} configuration used
     * by this end point.
     */
    private final PahoMqttConfiguration mConfiguration;

// --------------------------------------------------
    // CONSTRUCTOR
    // --------------------------------------------------

    /**
     *
     * @param endpoint
     * @param configuration
     *            The {@link PahoMqttConfiguration} configuration used by this
     *            end point definition.
     * @throws NotActiveException
     *             if the end point can not create an instance of
     *             {@link MqttClient}.
     */
    public PahoMqttProducer(PahoMqttEndpoint endpoint, PahoMqttConfiguration configuration) throws NotActiveException
    {
        super(endpoint);
        this.mConfiguration = configuration;

        try
        {
            PahoMqttProducer.LOG
                    .info("\t--> create mqtt client and connection options based on the end point configurations");
            // create a new instance of a MQTT client
            this.mMqttClient = new MqttClient(configuration.getHost(), configuration.getEndPointName(), null);
            // create new instance of MqttConnectOptions and fill in
            // configuration options
            this.mConnectOptions = new MqttConnectOptions();
            this.mConnectOptions.setCleanSession(configuration.isCleanSession());
            this.mConnectOptions.setConnectionTimeout(configuration.getConnectionTimeout());
        } catch (MqttException ex)
        {
            PahoMqttProducer.LOG.error("An error occurred while creating a MQTT client: {}", ex);
            throw new NotActiveException();
        }
    }

    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    /*
     * (non-Javadoc)
     * @see org.apache.camel.impl.DefaultProducer#doStart()
     */
    @Override
    protected void doStart() throws Exception
    {
        super.doStart();

        // connect the client to the broker
        this.mMqttClient.connect(this.mConnectOptions);
        PahoMqttProducer.LOG.info("\t--> connected to {}",
                PahoMqttProducer.this.mConfiguration.getHost());
    }

    /*
     * (non-Javadoc)
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception
    {
        if (this.mMqttClient.isConnected())
        {
            // get configuration from this endpoint
            PahoMqttConfiguration configuration = this.mConfiguration;

            // extract the payload from message
            byte[] payload = exchange.getIn().getBody(byte[].class);

            if (payload != null)
            {
                final String pubTopic = configuration.getPubTopicName();
                int qosLevel = configuration.getQosLevel();
                boolean retained = configuration.isRetained();

                this.mMqttClient.publish(pubTopic, payload, qosLevel, retained);
            }
            else
            {
                // no data was in the message body, so finish processing
                PahoMqttProducer.LOG
                        .info("\t--> No valid data to publish! Is your data really convertable into a byte array required by MQTT?");
            }
        }
        else
        {
            // client is not connected or lost connection --> reconnect and call
            // method again
            PahoMqttProducer.LOG.info("Client is not connected: --> so reconnect!");

            // reconnect the client to the broker
            this.mMqttClient.connect(this.mConnectOptions);
            try
            {
                PahoMqttProducer.this.process(exchange);
            } catch (Exception ex)
            {
                PahoMqttProducer.LOG.error(
                        "Paho MQTT Producer - failed to call process method again after reconnection: ", ex);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.camel.impl.DefaultProducer#doStop()
     */
    @Override
    protected void doStop() throws Exception
    {
        if (this.mMqttClient.isConnected())
        {
            PahoMqttProducer.LOG.info("\t--> try to disconnect MQTT client from {}",
                    PahoMqttProducer.this.mConfiguration.getHost());

            this.mMqttClient.disconnect();
            PahoMqttProducer.LOG.info("\t--> disconnected from {}",
                    PahoMqttProducer.this.mConfiguration.getHost());
        }

        super.doStop();
    }
}