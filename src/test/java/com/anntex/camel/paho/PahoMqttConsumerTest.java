/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.anntex.camel.paho;

import com.anntex.camel.paho.config.PahoMqttConfiguration;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;

/**
 * This class defines an integration test for testing the MQTT Apache Camel
 * gateway by consuming data from a MQTT server. <b>Please make sure your
 * environment has running a MQTT server to test against it.</b>
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttConsumerTest extends PahoMqttBaseTest
{
    // --------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------

    private static final String TEST_CLIENT_ID  = PahoMqttComponentTest.class.getSimpleName();
    private static final String TEST_PAYLOAD    = "This is a simple test message";

    // --------------------------------------------------
    // TESTS
    // --------------------------------------------------

    @Test(timeout = 60000)
    public void testSubscribeMqttMessageFromDefaultTopic() throws Exception
    {
        final MockEndpoint mock = this.getMockEndpoint("mock:result");
        this.context.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                // create a new route to test end point creation for certain
                // host url
                this.from("mqtt:testMessageFromDefaultTopic").to(mock);
            }
        });

        // publish a test message to the default topic to a mqtt server to
        // trigger the consumer component of the gateway
        PahoMqttConfiguration defaultConfig = new PahoMqttConfiguration();
        this.publishSomeTestMessageToMqttServer(defaultConfig.getPubTopicName());

        // sleep some time to wait for processing the information by the
        // consumer end point
        Thread.sleep(5000);

        // get the mocking end point and check if we received one message
        mock.expectedMinimumMessageCount(1);

        this.assertMockEndpointsSatisfied();
    }

    @Test(timeout = 60000)
    public void testSubscribeMqttMessageFromSpecificTopic() throws Exception
    {
        final MockEndpoint mock = this.getMockEndpoint("mock:result");
        final String subscription = PahoMqttConfiguration.DEFAULT_SUB_TOPIC_NAME + "=" + TEST_TOPIC_1;

        this.context.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                // create a new route to test end point creation for certain
                // host url
                this.from("mqtt:testMessageFromSpecificTopic?" + subscription).to(mock);
            }
        });

        // publish a test message to the default topic to a mqtt server to
        // trigger the consumer component of the gateway
        this.publishSomeTestMessageToMqttServer(TEST_TOPIC_1);

        // sleep some time to wait for processing the information by the
        // consumer end point
        Thread.sleep(10000);

        // get the mocking end point and check if we received one message
        mock.expectedMinimumMessageCount(1);

        this.assertMockEndpointsSatisfied();
    }

    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    /**
     * This method connects a paho mqtt client to a mqtt server to publish a
     * message to a certain topic. This is required to test the functionality of
     * the consumer of the Apache Camel Mqtt gateway.
     *
     * @param topic
     *            The name of the topic to publish a test message to.
     * @throws org.eclipse.paho.client.mqttv3.MqttException
     *             If an unexpected error occurred.
     */
    private void publishSomeTestMessageToMqttServer(String topic) throws MqttException
    {
        MqttClient testClient = new MqttClient("tcp://" + PahoMqttConsumerTest.DEFAULT_HOST,
                PahoMqttConsumerTest.TEST_CLIENT_ID);
        try
        {
            testClient.connect();
            MqttMessage message = new MqttMessage(PahoMqttConsumerTest.TEST_PAYLOAD.getBytes());
            testClient.publish(topic, message);
        } catch (Exception ex)
        {
            LOG.error("PahoMqttConsumer publish test message failed {}", ex);
        } finally
        {
            testClient.disconnect();
            testClient = null;
        }
    }
}
