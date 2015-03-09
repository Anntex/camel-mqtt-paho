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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

/**
 * This class defines test cases to test the creation of a MQTT Paho component for the Apache Camel project.
 * <b>Please make sure your environment has running a MQTT server to test against it.</b>
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttComponentTest extends PahoMqttBaseTest
{
    // --------------------------------------------------
    // CREATE_ROUTE_BUILDER
    // --------------------------------------------------

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception
    {
        return new RouteBuilder()
        {
            @Override
            public void configure()
            {
                this.from("mqtt://testNode").to(MOCK_RESULT);
            }
        };
    }

    // --------------------------------------------------
    // TESTS
    // --------------------------------------------------

    @Test(timeout = 60000)
    public void testCreateMqttComponent() throws Exception
    {
        MockEndpoint mock = this.getMockEndpoint(MOCK_RESULT);
        // expect no messages because we did not send some in this test case
        mock.expectedMinimumMessageCount(0);

        this.assertMockEndpointsSatisfied();
    }
}
