package com.example.webchatserver;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.util.Map.Entry;

import static com.example.util.ResourceAPI.loadChatRoomHistory;
import static com.example.util.ResourceAPI.saveChatRoomHistory;



public class Word{
    private static Map<String, String> wordList = new HashMap<>();
    private int randNum = 0;
    private int wordCount = 0;
    private int generateWordCount = 0;
    private String randomWord;
    private String randomDefinition;


    public Map<String, String> setWordList(){
        wordList.put("jovial", "cheerful and friendly");
        wordList.put("puppy", "a baby dog");
        wordList.put("absurd", "unreasonable or illogical");
        wordList.put("oxidize", "combine or become combined chemically with oxygen");
        wordList.put("bookworm", "a person that likes reading");
        wordList.put("abrupt", "sudden");
        wordList.put("galaxy", "what is the Milky Way called");
        wordList.put("blizzard", "a severe snowstorm with high winds and low visibility");
        wordList.put("awkward", "causing difficulty / hard to do or deal with");
        wordList.put("stronghold", "a fortified place");
        return wordList;
    }
    public int countWords(Map<String, String> currentList){
        for(String word : currentList.values()){
            wordCount++;
        }
        return wordCount;
    }
    public int generateNumber(int count){
        Random random = new Random();
        randNum = random.nextInt(count+1);
        return randNum;
    }

    public String generateWord(Map<String, String> currentList, int randomNum){
        int count = 0;
        for(Entry<String, String> word: currentList.entrySet()){
            if(count == randomNum){
               randomWord = word.getKey();
            }
            count++;
        }
        return randomWord;
    }

    public String generateDefinition(Map<String, String> currentList, int randomNum){
        int count = 0;
        for(Entry<String, String> word: currentList.entrySet()){
            if(count == randomNum){
                randomDefinition = word.getValue();
            }
            count++;
        }
        return randomDefinition;
    }


}