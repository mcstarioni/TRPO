package lab3;

import util.DatabaseConnector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrudServlet extends HttpServlet
{
    private HttpServletResponse response = null;
    private Connection con;
    private Map<String,CrudMethod> methodMap;
    private interface CrudMethod
    {
        void doCrud(Map<String,String> params);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.response = response;
        doStuff(request.getParameterMap());
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String path = "C:\\Users\\Макс\\Documents\\Programming\\6 semester\\TRPO\\src\\main\\web\\WEB-INF\\pages\\action.html";
        try (Stream<String> lines = Files.lines(Paths.get(path) ,
                Charset.defaultCharset()))
        {
            lines.forEach(response.getWriter()::println);
        }
    }
    private void doStuff(Map<String,String[]> paramsArray)
    {
        try
        {
            Map<String, String> params = paramsArray
                    .entrySet()
                    .stream()
                    .map(stringEntry ->
                            (Map.Entry<String, String>)
                                    new AbstractMap.SimpleEntry<String, String>
                                            (stringEntry.getKey(), stringEntry.getValue()[0]))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            String method = params.get("method");
            params.remove("method");
            this.con = DatabaseConnector.getConnection();
            ResultSetMetaData meta = con.createStatement()
                    .executeQuery("SELECT * FROM people")
                    .getMetaData();
            if(params.size() != meta.getColumnCount())
            {
                throw new Exception("html is shit");
            }
            methodMap.get(method).doCrud(params);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void doUpdateCrud(Map<String, String> params)
    {
        try
        {
            PreparedStatement update = con.prepareStatement("UPDATE people SET name = ?, salary = ? WHERE id = ?");
            update.setString(1, params.get("name"));
            update.setString(2, params.get("salary"));
            update.setString(3, params.get("id"));
            update.execute();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                response.sendRedirect("/outer");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private void doDeleteCrud(Map<String,String> params)
    {
        try{
            PreparedStatement delete = con.prepareStatement("DELETE FROM people WHERE id = ?");
            delete.setString(1, params.get("id"));
            delete.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                response.sendRedirect("/outer");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private void doReadCrud(Map<String,String> params)
    {
        try
        {
            ResultSet results = con.createStatement().executeQuery("SELECT * FROM people");
            PrintWriter writer = response.getWriter();
            writer.println(htmlOne + "<table BORDER=1 CELLPADDING=0 CELLSPACING=0 >"
                    +"<tr><th>ID</th><th>NAME</th><th>SALARY</th</tr>");
            while (results.next())
            {
                writer.println("<tr>");
                for (int i = 1; i <= results.getMetaData().getColumnCount(); i++)
                {
                    writer.print("<td>" + results.getString(i) + "</td>");
                }
                writer.println("</tr>");
            }
            writer.println("</table>" + htmlTwo);
        }
        catch (Exception e)
        {
            try
            {
                response.sendRedirect("/outer");
            }catch (Exception ex)
            {
                e.printStackTrace();
            }
        }
    }
    private void doCreateCrud(Map<String,String> params)
    {
        try
        {
            PreparedStatement create = con.prepareStatement("INSERT INTO people(name, salary) VALUES(?,?)");
            create.setString(1,params.get("name"));
            create.setString(2,params.get("salary"));
            create.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                response.sendRedirect("/outer");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public CrudServlet()
    {
        super();
        init();
    }
    public void init()
    {
        methodMap = new HashMap<>();
        methodMap.put("POST",this::doUpdateCrud);
        methodMap.put("GET",this::doReadCrud);
        methodMap.put("DELETE",this::doDeleteCrud);
        methodMap.put("PUT",this::doCreateCrud);
    }
    private String htmlOne = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Hello</title>\n" +
            "</head>\n" +
            "<body>";
    private String htmlTwo = "<a href = \"/outer\">Back to form</a>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
}
