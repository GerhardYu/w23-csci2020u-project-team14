package com.example.WordplayShowdown;

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
public class GameServlet extends HttpServlet {


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
        GameServer parsingRoomList = new GameServer();


        Map<String, String> roomList = parsingRoomList.getRoomList();

        Set<String> roomKeys = roomList.keySet();

        response.setContentType("text/plain");
        // send the random code as the response's content

        // get all existing rooms
        String room = "";

        for(String key: roomKeys){
            room += "," + roomList.get(key);
        }


        PrintWriter out = response.getWriter();
        // sends random roomID + previous created rooms + rooms with history chat log
        out.println("");

    }

    public void destroy() {
    }
}