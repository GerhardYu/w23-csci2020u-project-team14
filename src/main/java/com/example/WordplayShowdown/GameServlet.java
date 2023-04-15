package com.example.WordplayShowdown;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

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
        GameServer game = new GameServer();


        Map<String, String> players = game.getRoomPlayers();
        //Map<String, String> status = game.getOutputUserStatus();


        JSONObject jsonPlayers = new JSONObject(players);

        response.setContentType("application/json");
        // send the players as the response's content

        PrintWriter out = response.getWriter();

        System.out.println("here players before json");
        for (Map.Entry<String, String> entry : players.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        System.out.println("here players");
        System.out.println(jsonPlayers);

        // sends random roomID + previous created rooms + rooms with history chat log
        out.println(jsonPlayers);

    }

    public void destroy() {
    }
}