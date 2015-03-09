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
 * This class defines test methods to test the behavior of a MQTT Apache Camel end point. <b>Please make sure your
 * environment has running a MQTT server to test against it.</b>
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
public class PahoMqttEndpointTest extends PahoMqttBaseTest
{
    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    @Test(timeout = 60000)
    public void testCreateMqttEndpointByHost() throws Exception
    {
        this.context.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                // create a new route to test end point creation for certain host url
                this.from("mqtt:test?host=" + DEFAULT_HOST).to("mock:result");
            }
        });

        MockEndpoint mock = this.getMockEndpoint("mock:result");
        // we do not expect a message because in this test case we did not send one
        mock.expectedMinimumMessageCount(0);

        this.assertMockEndpointsSatisfied();
    }
}
