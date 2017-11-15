package db;

import util.Env;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static final String connection_string = "jdbc:mysql://127.0.0.1:3306/vrooom_ojek";
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

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>();
            for (int cnt = 1; cnt <= column_count; cnt++) row.add(resultSet.getString(cnt));

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

            database.update("INSERT INTO user VALUES (1, 'avatar', 'roland', 12345, 'roland@theavatar.com', NULL)");

            ArrayList<ArrayList<String>> result = new ArrayList<>();
            database.select("SELECT * FROM user", result);

            for(ArrayList<String> row : result) {
                int id = Integer.parseInt(row.get(0));
                String username = row.get(1);
                String name = row.get(2);
                String phone = row.get(3);
                String email = row.get(4);
                String profilePic = row.get(5);

                System.out.println("id = " + id);
                System.out.println("username = " + username);
                System.out.println("name = " + name);
                System.out.println("phone = " + phone);
                System.out.println("email = " + email);
                System.out.println("profilePic = " + profilePic);
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