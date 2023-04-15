package com.example.WordplayShowdown;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
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
    private Map<String, String> userStatus = new HashMap<String, String>();

    private static Map<String, String> roomList = new HashMap<String, String>();
    private static Map<String, String> roomPlayers = new HashMap<String, String>();
    private static Map<String, String> outputUserStatus = new HashMap<String, String>();

    private static Map<String, String> word = new HashMap<String, String>();

    private static Word randomWordList =  new Word(); //makes word class
    private static Map<String, String> wordList = randomWordList.setWordList(); //initialize words
    private static Integer randomNum = randomWordList.generateNumber(10); //generate a random number
    private static String currentWord = randomWordList.generateWord(wordList, randomNum); //get a random word based from the number randomly generated
    private static String currentDefinition = randomWordList.generateDefinition(wordList, randomNum); //gets the words definition

    private static boolean condition = false;
//

    // getter for roomList
    public Map<String, String> getRoomList()
    {
        return roomList;
    }

    // getter roomPlayers
    public Map<String, String> getRoomPlayers()
    {
        return roomPlayers;
    }

    // getter for outputUserStatus
    public Map<String, String> getOutputUserStatus()
    {
        return outputUserStatus;
    }

    @OnOpen
    public void open(@PathParam("roomID") String roomID, Session session) throws IOException, EncodeException {
        roomList.put(session.getId(), roomID); //adding userID to a room

        System.out.println("Room joined ");
        String wd = randomWordList.generateWord(wordList, randomNum);
        //word.put(currentWord, currentDefinition);


        session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(GameWord): " + currentWord + "," + currentDefinition + "\"}");
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
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + username + " left the game.\"}");
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


        System.out.println("current user = " + userID);

        if(usernames.containsKey(userID) && userStatus.get(userID).contains("ready")){ // not their first message
            String username = usernames.get(userID);
            System.out.println("username = ");
            System.out.println(username);
            message = message.toLowerCase();


            // broadcasting it to peers in the same room
            for(Session peer: session.getOpenSessions()){
                // only send my messages to those in the same room
                if (roomList.get(peer.getId()).equals(roomID)) {
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(" + username + "): " + message + "\"}");

                    if(message.equals(currentWord)){


                            // only announce to those in the same room as me, excluding myself
                        condition = true;
                        randomNum = randomWordList.generateNumber(10);

                    }
//                    else{
//                        peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): The word <" + message + "> inputted by " + username + " is wrong\"}");
//                        peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Please try again.\"}");
//                    }

//                            peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + message + " joined the game.\"}");
//                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Congratulations " + username + " you got it right!\"}");
//                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Correct word is " +  currentWord + "\"}");
//                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): A new word will now be generated.\"}");
//                    randomNum = randomWordList.generateNumber(10);
//                    currentWord = randomWordList.generateWord(wordList, randomNum);
//                    currentDefinition = randomWordList.generateDefinition(wordList, randomNum);
//                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(GameWord): " + currentWord + "," + currentDefinition + "\"}");


                }
                if(condition == true)
                {
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Congratulations " + username + " you got it right!\"}");
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Correct word is " +  currentWord + "\"}");
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): A new word will now be generated.\"}");
//                    randomNum = randomWordList.generateNumber(10);
                    currentWord = randomWordList.generateWord(wordList, randomNum);
                    currentDefinition = randomWordList.generateDefinition(wordList, randomNum);
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(GameWord): " + currentWord + "," + currentDefinition + "\"}");
                }
            }
            condition = false;
//            if(condition == true)
//            {
//                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Congratulations " + username + " you got it right!\"}");
//                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Correct word is " +  currentWord + "\"}");
//                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): A new word will now be generated.\"}");
//                randomNum = randomWordList.generateNumber(10);
//                currentWord = randomWordList.generateWord(wordList, randomNum);
//                currentDefinition = randomWordList.generateDefinition(wordList, randomNum);
//                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(GameWord): " + currentWord + "," + currentDefinition + "\"}");
//
//            }

        }
        else if(!usernames.containsKey(userID))
        { //first message is their username
            usernames.put(userID, message);
            userStatus.put(userID, message);


            roomPlayers.put(message, "no");

//            for (Map.Entry<String, String> entry : roomPlayers.entrySet()) {
//                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//            }

            //
            if (Collections.frequency(roomList.values(), roomID) == 1) {
                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): Welcome, " + message + "! Please wait for another play and click ready to play.\"}");
            } else {
                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome, " + message + "! Click ready to play.\"}");
            }

            // broadcasting it to peers in the same room
            for(Session peer: session.getOpenSessions()){
                // only announce to those in the same room as me, excluding myself
                if((!peer.getId().equals(userID)) && (roomList.get(peer.getId()).equals(roomID))){
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + message + " joined the game.\"}");
                }
            }
        }
        else
        {
            String username = usernames.get(userID);
            userStatus.put(userID, "ready");
            roomPlayers.put(username, "ready");
            userStatus.put(userID, userStatus.get(userID) + "," + message);

            for(Session peer: session.getOpenSessions()){
                // only send my messages to those in the same room
                if (roomList.get(peer.getId()).equals(roomID)) {
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(" + username + "): " + message + "\"}");
                }
            }
        }
    }
}
