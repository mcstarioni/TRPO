package util;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServletLauncher
{
    public static void launch(String descriptorName) throws Exception
    {
        Server server = new Server(8080);
        WebAppContext web = new WebAppContext();
        web.setContextPath("/");
        web.setWar("src/main/web");
        web.setDescriptor("src/main/web/WEB-INF/" + descriptorName);
        server.setHandler(web);
        server.start();
        server.join();
    }
}
