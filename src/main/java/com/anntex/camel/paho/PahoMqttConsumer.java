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

import java.io.NotActiveException;

import com.anntex.camel.paho.config.PahoMqttConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines a Paho MQTT consumer component which is required to
 * receive MQTT message from a MQTT server.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttConsumer extends DefaultConsumer
{
    // --------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(PahoMqttConsumer.class);

    /**
     * Defines a MQTT client based on the Paho implementation.
     */
    private final MqttAsyncClient       mMqttClient;

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
     * This constructor creates a new scheduled polling consumer for receiving
     * data from a MQTT broker and processing the data to the next route.
     *
     * @param endpoint
     *            The {@link PahoMqttEndpoint} definition to process received
     *            data.
     * @param processor
     *            The {@link Processor} definition to get the route to process
     *            the data to.
     * @param configuration
     *            The {@link PahoMqttConfiguration} configuration used by this
     *            end point definition.
     * @throws java.io.NotActiveException
     *             if the end point can not create an instance of
     *             {@link MqttAsyncClient}.
     */
    public PahoMqttConsumer(PahoMqttEndpoint endpoint, Processor processor, PahoMqttConfiguration configuration)
            throws NotActiveException
    {
        super(endpoint, processor);
        this.mConfiguration = configuration;

        try
        {
            PahoMqttConsumer.LOG
                    .info("\t--> create mqtt client and connection options based on the end point configurations");
            // create a new instance of a MQTT client
            this.mMqttClient = new MqttAsyncClient(configuration.getHost(), configuration.getEndPointName(), null);
            // create new instance of MqttConnectOptions and fill in
            // configuration options
            this.mConnectOptions = new MqttConnectOptions();
            this.mConnectOptions.setCleanSession(configuration.isCleanSession());
            this.mConnectOptions.setConnectionTimeout(configuration.getConnectionTimeout());
        }
        catch (MqttException ex)
        {
            PahoMqttConsumer.LOG.error("An error occurred while creating a MQTT client: {}", ex);
            throw new NotActiveException();
        }
    }

// --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    /*
     * (non-Javadoc)
     * @see org.apache.camel.impl.DefaultConsumer#getEndpoint()
     */
    @Override
    public PahoMqttEndpoint getEndpoint()
    {
        return (PahoMqttEndpoint) super.getEndpoint();
    }

    /**
     * @see org.apache.camel.impl.DefaultConsumer#doStart()
     */
    @Override
    protected void doStart() throws Exception
    {
        super.doStart();

        // get the end point to be able to forward incoming mqtt messages to the
        // next processor
        final PahoMqttEndpoint endpoint = PahoMqttConsumer.this.getEndpoint();

        // connect the client to the broker
        this.mMqttClient.connect(this.mConnectOptions, null, new IMqttActionListener()
        {
            @Override
            public void onSuccess(IMqttToken asyncActionToken)
            {
                PahoMqttConsumer.LOG.info("\t--> connected to {}",
                        PahoMqttConsumer.this.mConfiguration.getHost());

                // extract the values from configuration
                final String subTopic = PahoMqttConsumer.this.mConfiguration.getSubTopicName();
                int qos = PahoMqttConsumer.this.mConfiguration.getQosLevel();

                // set the callback function and handle logic for incoming
                // messages
                PahoMqttConsumer.this.mMqttClient.setCallback(new MqttCallback()
                {
                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception
                    {
                        // create a new exchange to pass information to the next
                        // end point
                        Exchange exchange = endpoint.createExchange();
                        exchange.getIn().setBody(message.getPayload());

                        // get the processing end point from the consumer and
                        // pass the exchange to the processor
                        Processor processor = PahoMqttConsumer.this.getProcessor();
                        processor.process(exchange);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token)
                    {
                        PahoMqttConsumer.LOG.info("Paho consumer - successful processing some data from {}",
                            subTopic);
                    }

                    @Override
                    public void connectionLost(Throwable cause)
                    {
                        PahoMqttConsumer.LOG.info("\t--> lost connection to the server! Reason: ", cause.getMessage());
                    }
                });

                try
                {
                    // now subscribe to the configured topic and wait for the
                    // callback to be called by the endpoint
                    PahoMqttConsumer.this.mMqttClient.subscribe(subTopic, qos);

                } catch (MqttException ex)
                {
                    PahoMqttConsumer.LOG.error("Paho consumer - error while subscribing data from topic: {}", subTopic);
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception)
            {
                PahoMqttConsumer.LOG.error("Paho MQTT Consumer - failed to connect to {}",
                        PahoMqttConsumer.this.mConfiguration.getHost());
            }
        });
    }

    /*
     * (non-Javadoc)
     * @see org.apache.camel.impl.DefaultConsumer#doStop()
     */
    @Override
    protected void doStop() throws Exception
    {
        if (this.mMqttClient.isConnected())
        {
            PahoMqttConsumer.LOG.info("\t--> try to disconnect MQTT client from {}",
                    PahoMqttConsumer.this.mConfiguration.getHost());

            this.mMqttClient.disconnect(null, new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    PahoMqttConsumer.LOG.info("\t--> disconnected from {}",
                            PahoMqttConsumer.this.mConfiguration.getHost());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    PahoMqttConsumer.LOG.error("\t--> failed to disconnect the MQTT client from {}",
                            PahoMqttConsumer.this.mConfiguration.getHost());
                }
            });
        }

        super.doStop();
    }
}
