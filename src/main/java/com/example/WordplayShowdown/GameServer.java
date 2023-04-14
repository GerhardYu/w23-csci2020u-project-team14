package com.example.WordplayShowdown;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a web socket server, a new connection is created and it receives a roomID as a parameter
 * **/
@ServerEndpoint(value="/ws/{roomID}")
public class GameServer {

    // contains a static List of ChatRoom used to control the existing rooms and their users

    // you may add other attributes as you see fit
    private Map<String, String> usernames = new HashMap<String, String>();
    private static Map<String, String> roomList = new HashMap<String, String>();

    private int point = 0; //points for getting a word right
    private Word randomWordList =  new Word(); //makes word class
    private Map<String, String> wordList = randomWordList.setWordList(); //initialize words
    private Integer wordCount = randomWordList.countWords(wordList); //counts words
    private Integer randomNum = randomWordList.generateNumber(wordCount); //generate a random number
    private String currentWord = randomWordList.generateWord(wordList, randomNum); //get a random word based from the number randomly generated
    private String currentDefinition = randomWordList.generateDefinition(wordList, randomNum); //gets the words definition


    // getter for roomList
    public Map<String, String> getRoomList()
    {
        return roomList;
    }

    // getter for roomHistoryList

    @OnOpen
    public void open(@PathParam("roomID") String roomID, Session session) throws IOException, EncodeException {
        roomList.put(session.getId(), roomID); //adding userID to a room
        
        System.out.println("Room joined ");

        System.out.println(roomID);

        session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome to the chat room. Please state your username to begin.\"}");
    }

    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        String userId = session.getId();
        // do things for when the connection closes
        if (usernames.containsKey(userId)) {
            String username = usernames.get(userId);
            String roomID = roomList.get(userId);
            usernames.remove(session.getId());
            // remove this user from the roomList
            roomList.remove(session.getId());


            // broadcasting it to peers in the same room
            int countPeers = 0;
            for (Session peer : session.getOpenSessions()){ //broadcast this person left the server
                if(roomList.get(peer.getId()).equals(roomID)) { // broadcast only to those in the same room
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + username + " left the chat room.\"}");
                    countPeers++; // count how many peers are left in the room
                }
            }
        }
    }

    @OnMessage
    public void handleMessage(String comm, Session session) throws IOException, EncodeException {
        String userID = session.getId(); // my id
        String roomID = roomList.get(userID); // my room
        JSONObject jsonmsg = new JSONObject(comm);
        String type = (String) jsonmsg.get("type");
        String message = (String) jsonmsg.get("msg");

        if(usernames.containsKey(userID)){ // not their first message
            String username = usernames.get(userID);
            System.out.println(username);
            message = message.toLowerCase();


            // broadcasting it to peers in the same room
            for(Session peer: session.getOpenSessions()){
                // only send my messages to those in the same room
                if (roomList.get(peer.getId()).equals(roomID)) {
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(" + username + "): " + message + "\"}");
                    if(message == currentWord){
                        peer.getBasicRemote().sendText(username + "got it right +1 point");
                        peer.getBasicRemote().sendText("Correct word is " +  currentWord);
                        point++;
                    }
                    else{
                        peer.getBasicRemote().sendText("The word inputted by " + username + " is wrong");
                        peer.getBasicRemote().sendText("Please try again");
                    }
                }
            }
        }else{ //first message is their username
            usernames.put(userID, message);
            session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome, " + message + "!\"}");

            // broadcasting it to peers in the same room
            for(Session peer: session.getOpenSessions()){
                // only announce to those in the same room as me, excluding myself
                if((!peer.getId().equals(userID)) && (roomList.get(peer.getId()).equals(roomID))){
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + message + " joined the chat room.\"}");
                }
            }
        }
    }
}
