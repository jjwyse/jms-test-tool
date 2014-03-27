package com.jjw.jmstesttool;

import com.jjw.jmstesttool.gui.MainFrame;
import com.tibco.tibjms.TibjmsConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;
import java.io.IOException;
import java.util.Properties;

/**
 * The main entry point of the program
 *
 * @author jjwyse
 * @version %I%, %G%
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class);

    private final static String ACTIVEMQ = "activemq";
    private final static String ACTIVEMQ_ENDPOINT = "activemq.endpoint";
    private final static String JMS = "jms";
    private final static String TIBCO = "tibco";
    private final static String TIBCO_ENDPOINT = "tibco.endpoint";
    private final static String PROPERTIES_FILE = "jms-test-tool.properties";

    public static void main(String[] args) throws Exception {

        CamelContext camelContext = new DefaultCamelContext();

        // command-line args
        String jmsArgument = args[0];
        if (StringUtils.isEmpty(jmsArgument)) {
            throw new RuntimeException(showUsage());
        }

        if (StringUtils.equals(jmsArgument, ACTIVEMQ)) {
            camelContext.addComponent(JMS, createActiveMqComponent());
        }
        else if (StringUtils.equals(jmsArgument, TIBCO)) {
            camelContext.addComponent(JMS, createTibcoComponent());
        }
        else {
            throw new RuntimeException(showUsage());
        }

        camelContext.start();

        new MainFrame(camelContext.createProducerTemplate());
    }

    protected static JmsComponent createActiveMqComponent() throws IOException {
        LOGGER.info("Attempting to connect to ActiveMQ");

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(getActiveMqEndpoint());

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(8);
        pooledConnectionFactory.setConnectionFactory(connectionFactory);

        JmsConfiguration jmsConfig = new JmsConfiguration(pooledConnectionFactory);
        jmsConfig.setConcurrentConsumers(10);

        JmsComponent jmsComponent = new ActiveMQComponent();
        jmsComponent.setConfiguration(jmsConfig);

        return jmsComponent;
    }

    protected static JmsComponent createTibcoComponent() throws IOException {
        LOGGER.info("Attempting to connect to Tibco");

        ConnectionFactory connectionFactory = new TibjmsConnectionFactory(getTibcoEndpoint());

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(connectionFactory);

        JmsConfiguration jmsConfig = new JmsConfiguration(cachingConnectionFactory);
        jmsConfig.setConcurrentConsumers(10);

        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(jmsConfig);

        return jmsComponent;
    }

    private static String getActiveMqEndpoint() throws IOException {
        return loadProperties().getProperty(ACTIVEMQ_ENDPOINT);
    }

    private static String getTibcoEndpoint() throws IOException {
        return loadProperties().getProperty(TIBCO_ENDPOINT);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));

        return properties;
    }

    private static String showUsage() {
        return "Usage: java -jar jms-test-tool-[version].jar [activemq|tibco]";
    }

}
