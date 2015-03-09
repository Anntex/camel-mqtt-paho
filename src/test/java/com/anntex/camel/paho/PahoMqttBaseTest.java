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

import org.apache.camel.test.junit4.CamelTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines a base class for all Paho Mqtt test cases.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttBaseTest extends CamelTestSupport
{
    // --------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------

    /**
     * The base logger for all Paho MQTT tests.
     */
    protected static final Logger LOG                   = LoggerFactory.getLogger(PahoMqttBaseTest.class);

    // --------------------------------------------------
    //  SOME TEST TOPIC DEFINITIONS
    protected static final String TEST_TOPIC_1          = "test/camel/paho";
    protected static final String TEST_TOPIC_2          = "test/paho";
    protected static final String TEST_WILDCARD_TOPIC_1 = "test/paho/+";
    protected static final String TEST_WILDCARD_TOPIC_2 = "test/paho/#";
    protected static final String TEST_WILDCARD_TOPIC_3 = "test/+/#";
    protected static final String TEST_TOPICS           = TEST_TOPIC_1 + "," + TEST_TOPIC_2;
    protected static final int    TEST_QOS_LEVEL        = 1;
    // --------------------------------------------------

    protected static final String DEFAULT_HOST          = "127.0.0.1:1883";


    // --------------------------------------------------
    // SOME ENDPOINT ROUT DEFINITIONS
    protected static final String DIRECT_START          = "direct:start";
    protected static final String MOCK_RESULT           = "mock:result";
    // --------------------------------------------------

    // --------------------------------------------------
    // METHOD
    // -------------------------------------------------

    /**
     * The method creates an endpoint definition based on the given route information.
     *
     * @param routeDefinition The camel route definition parameters as string
     * @return {@link com.anntex.camel.paho.PahoMqttEndpoint}
     */
    protected PahoMqttEndpoint createEndpointDefinition(String routeDefinition)
    {
        return (PahoMqttEndpoint) this.context.getEndpoint("mqtt:test?" + routeDefinition);
    }
}
