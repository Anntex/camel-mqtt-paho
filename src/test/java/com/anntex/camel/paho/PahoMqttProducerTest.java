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

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

/**
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttProducerTest extends PahoMqttBaseTest
{
    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    @Test(timeout = 60000)
    public void testPublishMqttMessageToDefinedTopic() throws Exception
    {
        byte[] payload = "Hello Camel mqtt example".getBytes();

        // create a new route builder
        this.context.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                // create a new route to test end point creation for certain host url
                this.from("direct:start").to(
                        "mqtt:testMessageToDefaultTopic?pubTopicName=" + PahoMqttProducerTest.TEST_TOPIC_1);
            }
        });

        this.template.sendBodyAndHeader("mqtt:testMessageToDefaultTopic?pubTopicName="
                + PahoMqttProducerTest.TEST_TOPIC_1, payload, Exchange.TO_ENDPOINT, PahoMqttProducerTest.TEST_TOPIC_1);

        Thread.sleep(3000);

        this.assertMockEndpointsSatisfied();
    }
}
