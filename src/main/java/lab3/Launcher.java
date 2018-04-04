package lab3;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.lang.management.ManagementFactory;

public class Launcher
    {
        public static void main(String[] args) throws Exception
        {
            Server server = new Server(8080);
            WebAppContext web = new WebAppContext();
            web.setContextPath("/");
            web.setWar("src/main/web");
            web.setDescriptor("src/main/web/WEB-INF/web.xml");
            //web.setAttribute("javax.servlet.context.tempdir",scratchDir);
            server.setHandler(web);
            server.start();
            server.join();
        }
    }
