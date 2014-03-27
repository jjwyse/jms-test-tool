package com.jjw.jmstesttool.route;

import com.jjw.jmstesttool.jaxb.Note;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

/**
 * Configures any listening routes for testing purposes
 *
 * @author jjwyse
 * @version %I%, %G%
 */
public class CamelRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        JaxbDataFormat jaxb = new JaxbDataFormat(true);
        jaxb.setContextPath("com.jjw.jmstesttool.jaxb");

        // listens on direct:testing and sends to our logging endpoint
        from("direct:testing").to("direct:logging");

        // listens on direct:note, unmarshals object and sends to our logging endpoint
        from("direct:note").unmarshal(jaxb).to("direct:logging");

        // listens on jms:queue:testing and sends to our logging endpoint
        from("jms:queue:testing").to("direct:logging");

        // listens on direct:logging and then logs a message
        from("direct:logging").to("log:com.jjw.jmstesttool?level=INFO&showBody=true");
    }

    private Note createNote() {
        return new Note();
    }
}
