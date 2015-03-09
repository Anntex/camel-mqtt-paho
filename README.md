# MQTT Apache Camel component based on Eclipse MQTT Paho

The following project provides source for an Apache Camel component based on the MQTT Paho project of the Eclipse
Foundation (see https://eclipse.org/paho/). This component was developed as part of a study work, since the original
available MQTT camel component could not successfully communicate with the MQTT RSMB broker implementation.

## Installation & Usage
The component project is based on Apache Maven which offers you a convenient way to install the component in your local repository and reuse it in many projects. You need to run the installation goal of Apache Maven in the source directory of the  project. Maven will look for the pom.xml file and will install the project on your deployment system. If you run the project without to skip the test goal you need to have running a MQTT server on your machine. This is required to run the integration tests of the project. Run the following command in the command line or as a Maven build in your IDE (or use the second command to skip the tests):
```shell
mvn package install
```
```shell
mvn package install -DskipTests
```
After the installation, the component can be used in your Apache Camel projects.
You have two options to include the Paho Mqtt component into your project:

* include the packaged jar file into your project
* use Apache Maven to include the source for you

### Include
Include the following dependency into your pom.xml file of your Apache Camel project:

```xml
<properties>
	<camel.paho.version>0.9-SNAPSHOT</camel.paho.version>
</properties>

<dependencies>
	<!-- other dependencies -->
	<dependency>
		<groupId>com.anntex.camel.paho</groupId>
		<artifactId>camel-mqtt-paho</artifactId>
		<version>${camel.paho.version}</version>
	</dependency>
</dependencies>
```

## Route Definitions
The following example will show you how to use the component in your camel route definitions.

```java
// as a producer
from("direct:start")
	.to("mqtt:producerPoint?host=127.0.0.1:1883&pubTopicName=/camel/mqtt/test&qosLevel=1&retained=true");

// as a consumer
from("mqtt://consumerPoint?host=127.0.0.1:1883&subTopicName=/camel/mqtt/test")
	.to("mock:result");
```

## Component options
The following table shows the supported component options which can be used in the route definitions.

| Option  | Default  | Description |
| :------------:|:---------------:| :-----|
| endPointName  | camel-paho-mqtt | Defines the mqtt client ID for this enpoint. Max. 23 characters in MQTT version 3.1 |
| host      | 127.0.0.1:1883      |   Defines the URL of the MQTT server the end point should be connected to. |
| pubTopicName | camel/mqtt/test | Defines the default publish topic name. This topic will be used to publish a other world message to a MQTT server and into the MQTT world. |
| subTopicName | # |Defines the default subscribe topic name. This topic will be used to subscribe messages from a MQTT server and send them into the other protocol world.|
| qosLevel | 0 | Defines the default Qos level value of MQTT. The end point only support the level 0 = AT_MOST_ONCE, 1 = AT_LEAST_ONCE and 2 = AT_EXACTLY_ONCE|
| connectionTimeout | 10000 | Defines the default connection time out value used by the subscription connection. If keep alive messages has a delay greater than 10 seconds the communication between the MQTT broker and the Camel end point will be broke down.|
| cleanSession | true | Defines the default flag if setting up a connection between the MQTT server and the Camel end point should be cleaned before a new connection can be established. If this flag is set to false and there was a session between the server and the end point before, MQTT reuses this session. |
| retained | false | Defines the default flag if a published message should be marked as retained. This can be helpful for late joiners. |

## Changes & Versions
This section provides information about the different changes, milestones and versions of this project.

| Version  | Date  | Description |
| :------------:|:---------------:| :-----|
| 0.9-SNAPSHOT  | March 09, 2015 | Implementation of the required Apache Camel components like Endpoint, Consumer, Producer, etc. Implementation to consume and produce MQTT message based on the Eclipse MQTT Paho client written in Java. Implementation of JUni test cases to test the functionality of the created Consumer and Producer components |
| 0.1-SNAPSHOT      | March 04, 2015      |   Initial commit |
