package db;

import java.sql.*;
import java.util.*;

public class Database {

    private static final String connection_string = "jdbc:mysql://127.0.0.1:3306/vrooom_account";
    private final String username;
    private final String password;
    private Connection connection;

    public Database() throws Exception {

        connection = null;

        Env env = new Env();
        username = env.getUsername();
        password = env.getPassword();
    }

    public void openConnection() throws Exception {
        connection = DriverManager.getConnection(connection_string, username, password);
    }

    public void closeConnection() throws Exception {
        connection.close();
    }

    public boolean update(String query) throws Exception {
        Statement statement = connection.createStatement();
        int rows = statement.executeUpdate(query);
        statement.close();

        return (rows != 0);
    }

    public ArrayList<ArrayList<String>> select(String query) throws Exception {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ResultSetMetaData metadata = resultSet.getMetaData();
        int column_count = metadata.getColumnCount();

        while(resultSet.next()) {
            ArrayList<String> row = new ArrayList<>();
            for(int cnt=1; cnt<=column_count; cnt++) row.add(resultSet.getString(cnt));

            result.add(row);
        }

        resultSet.close();
        statement.close();

        return result;
    }

    /*
    public static void main(String[] args) {
        try {
            db.Database database = new db.Database();

            database.openConnection();

            database.update("INSERT INTO user VALUES (1, 'avatar', 'email', 'roland', 12345)");
            database.update("INSERT INTO user VALUES (2, 'avatara', 'email2', 'rolanda', 123456)");
            database.update("INSERT INTO user VALUES (3, 'avatarb', 'email3', 'rolandaa', 123457)");

            ArrayList<ArrayList<String>> result = database.select("SELECT * FROM user");

            for(ArrayList<String> row : result) {
                int id = Integer.parseInt(row.get(0));
                String username = row.get(1);
                String email = row.get(2);
                String password = row.get(3);
                String token = row.get(4);

                System.out.println("id = " + id);
                System.out.println("username = " + username);;
                System.out.println("email = " + email);
                System.out.println("password = " + password);
                System.out.println("token = " + token);
                System.out.println("----------------------------");
            }

            database.closeConnection();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    */

}