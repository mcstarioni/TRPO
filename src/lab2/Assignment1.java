package lab2;

import com.google.gson.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.lang.*;
public class Assignment1
{
    private static final String url = "jdbc:mysql://207.154.214.188:3306/trpo";
    private static final String user = "trpo";
    private static final String password = "trpo";
    private static Connection connection;
    public static void main(String[] args) throws Exception
    {
        Gson gson = new Gson();
        String jsonFile = "";
        FileInputStream fi = new FileInputStream(
                "C:\\Users\\Макс\\Documents\\Programming\\" +
                "6 semester\\TRPO\\src\\lab2\\E05_aanderaa_all_1769_d432_5004.json");
        jsonFile = readFromJson(fi);
        JsonObject table = gson.fromJson(jsonFile, JsonObject.class).get("table").getAsJsonObject();
        final ArrayList<JsonArray> fields = new ArrayList<>();
        table.keySet().forEach((String s) -> fields.add(table.get(s).getAsJsonArray()));
        writeToDatabase(fields);
    }
    private static String readFromJson(InputStream inputStream)
    {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    private static void writeToDatabase(ArrayList<JsonArray> fields)
    {
        try
        {
            connection = DriverManager.getConnection(url, user, password);
            String tableName = "Weather_station";
            createTable(fields,tableName);
            writeTable(fields,tableName);
            connection.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    private static void createTable(ArrayList<JsonArray> fields, String tableName) throws SQLException
    {
        Statement stmt = connection.createStatement();
        StringBuilder attributes = new StringBuilder();
        Iterator<JsonElement> iter = fields.get(0).iterator();
        fields.get(1).iterator().forEachRemaining(
            element ->
            {
                attributes.append(iter.next().getAsString())
                        .append(" ")
                        .append(typesToDatabase.get(element.getAsString()))
                        .append(" not null,\n ");
            }
        );
        String query = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(" +
                        "id INTEGER(20) NOT NULL AUTO_INCREMENT, " +
                        attributes.toString() +
                        "PRIMARY KEY(id));";
        stmt.executeUpdate(query);
        stmt.close();
    }
    private static void writeTable(ArrayList<JsonArray> fields, String tableName) throws SQLException
    {
        StringBuilder values = new StringBuilder();
        fields.get(3).iterator().forEachRemaining(
            array ->
            {
                values.append("(null,");
                array.getAsJsonArray().iterator().forEachRemaining(
                    element ->
                    {
                        String quotes = (element.getAsJsonPrimitive().isString())?"'":"";
                        values.append(quotes)
                                .append(element.getAsString())
                                .append(quotes)
                                .append(",");
                    });
                values.deleteCharAt(values.length() - 1);
                values.append("),\n");
            });
        String query = "INSERT INTO " + tableName +
                " VALUES" + values.substring(0,values.length() - 2) + ";";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }
    private static Map<String,String> typesToDatabase;
    private static JsonElement element;
    static
    {
        typesToDatabase = new HashMap<>();
        typesToDatabase.put("String","VARCHAR(50)");
        typesToDatabase.put("float","FLOAT(20)");
        typesToDatabase.put("byte","TINYINT(20)");
    }
}
