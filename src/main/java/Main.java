import gui.MainFrame;
import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO - JJW
 *
 * @author jjwyse
 * @version %I%, %G%
 */
public class Main
{
    public static void main(String[] args)
    {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"camel-context.xml"});
        CamelContext camelContext = (CamelContext) context.getBean("camelContext");

        new MainFrame(camelContext.createProducerTemplate());
    }
}
