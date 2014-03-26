import gui.MainFrame;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;

/**
 * The main entry point of the program
 *
 * @author jjwyse
 * @version %I%, %G%
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(8);
        pooledConnectionFactory.setConnectionFactory(connectionFactory);

        JmsConfiguration jmsConfig = new JmsConfiguration(pooledConnectionFactory);
        jmsConfig.setConcurrentConsumers(10);

        JmsComponent jmsComponent = new ActiveMQComponent();
        jmsComponent.setConfiguration(jmsConfig);

        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addComponent("jms", jmsComponent);
        camelContext.start();

        new MainFrame(camelContext.createProducerTemplate());
    }
}
