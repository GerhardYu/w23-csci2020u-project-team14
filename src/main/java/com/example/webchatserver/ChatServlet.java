package com.example.webchatserver;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * This is a class that has services
 * In our case, we are using this to generate unique room IDs**/
@WebServlet(name = "chatServlet", value = "/chat-servlet")
public class ChatServlet extends HttpServlet {


    private String message;

    //static so this set is unique
    public static Set<String> rooms = new HashSet<>();



    /**
     * Method generates unique room codes
     * **/
    public String generatingRandomUpperAlphanumericString(int length) {
        String generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        // generating unique room code
        while (rooms.contains(generatedString)){
            generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        }
        rooms.add(generatedString);

        return generatedString;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ChatServer parsingRoomList = new ChatServer();

        Map<String, String> roomList = parsingRoomList.getRoomList();
        Map<String, String> historyList = parsingRoomList.getRoomHistoryList();

        Set<String> roomKeys = roomList.keySet();
        Set<String> historyKeys = historyList.keySet();

        response.setContentType("text/plain");
        // send the random code as the response's content

        // get all existing rooms
        String room = "";

        // get all history rooms: rooms with chat history, and current user is 0
        String history = ",history";

        for(String key: roomKeys){
            room += "," + roomList.get(key);
        }
        for(String key: historyKeys){
            history += "," + key;
        }

        PrintWriter out = response.getWriter();
        // sends random roomID + previous created rooms + rooms with history chat log
        out.println(generatingRandomUpperAlphanumericString(5) + room + history);

    }

    public void destroy() {
    }
}