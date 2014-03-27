package com.jjw.jmstesttool.route;

import org.apache.camel.builder.RouteBuilder;

/**
 * TODO - JJW
 *
 * @author jjwyse
 * @version %I%, %G%
 */
public class CamelRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:testing").to("log:com.jjw.jmstesttool?level=INFO&showAll=true");
    }
}
