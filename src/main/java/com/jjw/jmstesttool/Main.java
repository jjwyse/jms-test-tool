package com.jjw.jmstesttool;

import com.jjw.jmstesttool.gui.MainFrame;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.jms.ConnectionFactory;

/**
 * The main entry point of the program
 *
 * @author jjwyse
 * @version %I%, %G%
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    private final static String ACTIVEMQ = "activemq";
    private final static String JMS = "jms";
    private final static String TIBCO = "tibco";

    public static void main(String[] args) throws Exception {

        CamelContext camelContext = new DefaultCamelContext();

        // command-line args
        String jmsArgument = args[0];
        if (StringUtils.isEmpty(jmsArgument)) {
            throw new RuntimeException(showUsage());
        }

        if (StringUtils.equals(jmsArgument, ACTIVEMQ)) {
            LOGGER.info("Attempting to connect to ActiveMQ");
            camelContext.addComponent(JMS, createActiveMqComponent());
        }
        else if (StringUtils.equals(jmsArgument, TIBCO)) {
            LOGGER.info("Attempting to connect to Tibco");
            camelContext.addComponent(JMS, createTibcoComponent());
        }
        else {
            throw new RuntimeException(showUsage());
        }

        camelContext.start();

        new MainFrame(camelContext.createProducerTemplate());
    }

    protected static JmsComponent createActiveMqComponent() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(8);
        pooledConnectionFactory.setConnectionFactory(connectionFactory);

        JmsConfiguration jmsConfig = new JmsConfiguration(pooledConnectionFactory);
        jmsConfig.setConcurrentConsumers(10);

        JmsComponent jmsComponent = new ActiveMQComponent();
        jmsComponent.setConfiguration(jmsConfig);

        return jmsComponent;
    }

    protected static JmsComponent createTibcoComponent() {
        throw new RuntimeException("Tibco not implemented");
    }

    private static String showUsage() {
        return "Usage: java -jar jms-test-tool-[version].jar [activemq|tibco]";
    }

}
