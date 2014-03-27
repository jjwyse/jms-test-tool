# jms-test-tool
Java Swing GUI to send messages to a JMS instance

## setup

* Add a jms-test-tool.properties file to the resources directory with the following properties:
```
activemq.endpoint = tcp://<url>:<port>
tibco.endpoint    = tcp://<url>:<port>
```

* Upload the tibco JMS JAR to your local Maven repository or comment out those lines in the POM if you're using only ActiveMQ

* Run the Main class with activemq or tibco as a program argument