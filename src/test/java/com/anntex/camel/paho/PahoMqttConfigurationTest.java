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
import org.junit.Test;

/**
 * This test class defines unit tests to ensure the mapping of endpoint configuration information to the
 * {@link com.anntex.camel.paho.config.PahoMqttConfiguration} class.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttConfigurationTest extends PahoMqttBaseTest
{
    // --------------------------------------------------
    // TEST
    // --------------------------------------------------

    @Test
    public void testDefaultConfiguration() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("pubTopicName=" + TEST_TOPIC_1);
        PahoMqttConfiguration defaultConfig = new PahoMqttConfiguration();

        assertEquals(endpoint.getConfiguration().getHost(), defaultConfig.getHost());
        assertEquals(endpoint.getConfiguration().getPubTopicName(), TEST_TOPIC_1);
        assertEquals(endpoint.getConfiguration().getSubTopicName(), defaultConfig.getSubTopicName());
        assertEquals(endpoint.getConfiguration().getConnectionTimeout(), defaultConfig.getConnectionTimeout());
        assertEquals(endpoint.getConfiguration().getQosLevel(), defaultConfig.getQosLevel());
        assertEquals(endpoint.getConfiguration().isCleanSession(), defaultConfig.isCleanSession());
        assertEquals(endpoint.getConfiguration().isRetained(), defaultConfig.isRetained());
    }

    @Test
    public void testHostDefinition() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("host=" + DEFAULT_HOST);

        assertEquals(endpoint.getConfiguration().getHost(), "tcp://" + DEFAULT_HOST);
    }

    @Test
    public void testPublishTopicDefinition() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("pubTopicName=" + TEST_TOPIC_1);

        assertEquals(endpoint.getConfiguration().getPubTopicName(), TEST_TOPIC_1);
    }

// TODO next version
//    @Test
//    public void testMultiplePublishTopicDefinition() throws Exception
//    {
//        assertTrue(false);
//    }

    @Test
    public void testRetainDefinition() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("retained=true");

        assertEquals(endpoint.getConfiguration().isRetained(), true);
    }

    @Test
    public void testSubscribeTopicDefinition() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("subTopicName=" + TEST_TOPIC_2);

        assertEquals(endpoint.getConfiguration().getSubTopicName(), TEST_TOPIC_2);
    }

// TODO next version
//    @Test
//    public void testMultipleSubscribeTopicDefinition() throws Exception
//    {
//        assertTrue(false);
//    }

    @Test
    public void testQoSLevelDefinition() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("qosLevel=" + TEST_QOS_LEVEL);

        assertEquals(endpoint.getConfiguration().getQosLevel(), TEST_QOS_LEVEL);
    }

    @Test
    public void testNotAllowedQoSLevelDefinition() throws Exception
    {
        // the only allowed QoS values are 0 >= x <= 2
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("qosLevel=" + 10);

        // assume default qos level definition as fallback
        assertEquals(endpoint.getConfiguration().getQosLevel(), 0);
    }

    @Test
    public void testCleanSessionDefinition() throws Exception
    {
        PahoMqttEndpoint endpoint = this.createEndpointDefinition("cleanSession=false");

        assertEquals(endpoint.getConfiguration().isCleanSession(), false);
    }
}
