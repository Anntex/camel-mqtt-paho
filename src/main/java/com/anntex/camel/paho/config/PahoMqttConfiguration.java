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
package com.anntex.camel.paho.config;

import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

/**
 * This configuration class defines some essential MQTT properties required to
 * open up a connection to a MQTT server. The configuration supports only a
 * minimum set of MQTT Paho properties. This component can be extended to support
 * more features of MQTT like authentication, last will, etc.
 *
 * @author dennis.grewe [https://github.com/Anntex]
 * @version 0.9-SNAPSHOT March 04, 2015
 */
@UriParams
public class PahoMqttConfiguration
{
    // --------------------------------------------------
    // PROPERTIES
    // --------------------------------------------------

    public static final String DEFAULT_QOS_LEVEL_NAME          = "qosLevel";
    public static final String DEFAULT_PUB_TOPIC_NAME          = "pubTopicName";
    public static final String DEFAULT_SUB_TOPIC_NAME          = "subTopicName";
    public static final String DEFAULT_IS_RETAINED_NAME        = "retained";
    public static final String DEFAULT_IS_CLEAN_SESSION_NAME   = "cleanSession";
    public static final String DEFAULT_CONNECTION_TIMEOUT_NAME = "connectionTimeout";

    @UriParam
    private String             endPointName                    = "camel-paho-mqtt";
    @UriParam
    private String             host                            = "127.0.0.1:1883";
    @UriParam
    private String             pubTopicName                    = "camel/mqtt/test";
    @UriParam
    private String             subTopicName                    = "#";

    /**
     * Defines the default QoS level for the MQTT client.
     */
    @UriParam
    private int                qosLevel                        = 0;
    @UriParam
    private int                connectionTimeout               = 10000;
    @UriParam
    private boolean            cleanSession                    = true;
    @UriParam
    private boolean            retained                        = false;

    // --------------------------------------------------
    // METHODS
    // --------------------------------------------------

    /**
     * @return the host
     */
    public String getHost()
    {
        return "tcp://" + this.host;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host)
    {
        this.host = host;
    }

    /**
     * @return the pubTopicName
     */
    public String getPubTopicName()
    {
        return this.pubTopicName;
    }

    /**
     * @param pubTopicName
     *            the pubTopicName to set
     */
    public void setPubTopicName(String pubTopicName)
    {
        this.pubTopicName = pubTopicName;
    }

    /**
     * @return the subTopicName
     */
    public String getSubTopicName()
    {
        return this.subTopicName;
    }

    /**
     * @param subTopicName
     *            the subTopicName to set
     */
    public void setSubTopicName(String subTopicName)
    {
        this.subTopicName = subTopicName;
    }

    /**
     * @return the qosLevel
     */
    public int getQosLevel()
    {
        return this.qosLevel;
    }

    /**
     * @param qosLevel
     *            the qosLevel to set
     */
    public void setQosLevel(int qosLevel)
    {
        if (qosLevel >= 0 && qosLevel <= 2)
        {
            this.qosLevel = qosLevel;
        }
    }

    /**
     * @return the cleanSession
     */
    public boolean isCleanSession()
    {
        return this.cleanSession;
    }

    /**
     * @param cleanSession
     *            the isCleanSession to set
     */
    public void setCleanSession(boolean cleanSession)
    {
        this.cleanSession = cleanSession;
    }

    /**
     * @return the retained
     */
    public boolean isRetained()
    {
        return this.retained;
    }

    /**
     * @param retained
     *            the isRetained to set
     */
    public void setRetained(boolean retained)
    {
        this.retained = retained;
    }

    /**
     * @return the connectionTimeout
     */
    public int getConnectionTimeout()
    {
        return this.connectionTimeout;
    }

    /**
     * @param connectionTimeout
     *            the connectionTimeout to set
     */
    public void setConnectionTimeout(int connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * @return the endPointName
     */
    public String getEndPointName()
    {
        return this.endPointName;
    }

    /**
     * @param endPointName
     *            the endPointName to set
     */
    public void setEndPointName(String endPointName)
    {
        this.endPointName = endPointName;
    }
}