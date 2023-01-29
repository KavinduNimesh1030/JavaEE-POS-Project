package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {

       /* BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/thogakade");
        bds.setUsername("root");
        bds.setPassword("root1234");
        connection = bds.getConnection();*/

      /*  Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "root1234");
*/
    }

    public static DBConnection getDbConnection() throws SQLException, ClassNotFoundException {
        return (dbConnection == null) ? dbConnection = new DBConnection() : dbConnection;

    }

    public Connection getConnection() {
        return connection;
    }
}




