package util;

import java.io.File;
import java.util.Scanner;

public class Env {

    private String username;
    private String password;
    private int ojek_port;
    private int identity_port;

    public Env() throws Exception {

        File file = new File(".env");
        Scanner scanner = new Scanner(file);

        username = "util";
        password = "";
        identity_port = 8081;
        ojek_port = 8082;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String data[] = line.split("=");
            if (data.length == 2) {
                if (data[0].equals("DB_USERNAME")){
                    username = data[1];
                }

                if (data[0].equals("DB_PASSWORD")) {
                    password = data[1];
                }

                if (data[0].equals("OJEK_PORT")){
                    ojek_port = Integer.parseInt(data[1]);
                }

                if (data[0].equals("IDENTITY_PORT")) {
                    identity_port = Integer.parseInt(data[1]);
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getOjekPort() { return ojek_port; }

    public int getIdentityPort() { return identity_port; }
}