package com.example.util;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ResourceAPI {

    public void printmsg()
    {
        System.out.print("in resource api");
    }
    /**
     * This method call the ChatResourceAPI to save the log history of a room
     * */
    public static void saveChatRoomHistory(String roomID, String log) throws IOException {
        String uriAPI = "http://localhost:8080/ChatResourceAPI-1.0-SNAPSHOT/api/history/"+roomID;
        URL url = new URL(uriAPI);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        // allows us to write content to the outputStream
        con.setDoOutput(true);

        // sending the data with the POST request
        String jsonInputString = "{\"room\":\""+roomID+"\",\"log\":\""+log+"\"}";
        System.out.println(jsonInputString);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //reading and printing response
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * This method call the ChatResourceAPI to load the log history of a room
     * */
    public static String loadChatRoomHistory(String roomID) throws IOException {
        String uriAPI = "http://localhost:8080/ChatResourceAPI-1.0-SNAPSHOT/api/history/"+roomID;
        URL url = new URL(uriAPI);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        // we dont need to write content to the outputStream
        con.setDoOutput(false);
        // allows us to read inputstream
        con.setDoInput(true);

        // getting the inputStream
        InputStream inStream = con.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        String jsonData = buffer.toString();

        System.out.println("load the data");
        System.out.println(jsonData);

        // transforming the string into objects using org.json library
        JSONObject data = new JSONObject(jsonData);
        Map<String, Object> mapData = data.toMap();
        String content = (String) mapData.get("log");

        return content;

    }

}
