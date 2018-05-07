package lab4;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class HtmlServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        //super.doGet(req, resp);
        System.out.println("DO GET http");
        String path = "C:\\Users\\Макс\\Documents\\Programming\\6 semester\\" +
                "TRPO\\src\\main\\web\\WEB-INF\\pages\\CurrencyPage.html";
        try (Stream<String> lines = Files.lines(Paths.get(path) ,
                Charset.defaultCharset()))
        {
            lines.forEach(resp.getWriter()::println);
        }
    }
}
