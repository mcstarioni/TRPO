package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector
{
    private static final String url = "jdbc:mysql://207.154.214.188:3306/trpo";
    private static final String user = "trpo";
    private static final String password = "trpo";
    public static Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
