package com.hotmail.solntsev_igor;

import java.sql.*;

/**
 * Created by solncevigor on 3/27/17.
 */
public class DataBaseManager {
    //jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/MeasureDB?useSSL=false&serverTimezone=UTC";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "157613";

    static Connection connection;



    public static void initDB() throws SQLException{
        connection = DriverManager.getConnection(
                DB_CONNECTION,
                DB_USER,
                DB_PASSWORD);
        Statement st = connection.createStatement();

        try {

           // st.execute("DROP TABLE IF EXISTS MeasureData");
          //  st.execute("CREATE TABLE MeasureData (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, data VARCHAR (20) NOT NULL) ");
        }finally {
            st.close();
        }
    }

    public static void addData(String data, String date) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("INSERT INTO MeasureData (data, date) VALUE (?,?)");
        try {
            ps.setString(1, data);
            ps.setString(2, date);
            ps.executeUpdate();
        }finally {
            ps.close();
        }
    }
}
