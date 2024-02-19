package orlov641p.khai.edu.com.controller.lab3url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaseClient {
    public static String makeConnection(String request){
        try {
            URL url = new URL(request);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            StringBuilder result = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }

            connection.disconnect();

            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}