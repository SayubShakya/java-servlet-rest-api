package com.sayub;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Application {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        WebAppContext context = new WebAppContext();

        context.setContextPath("/");
        context.setResourceBase("src/main/webapp");
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/target/classes/.*");
        context.setParentLoaderPriority(true);
        context.setConfigurationDiscovered(true);

        server.setHandler(context);
        server.start();
        server.join();
    }
}
