package com.example.WordplayShowdown;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

//import static com.example.util.ResourceAPI.loadChatRoomHistory;
//import static com.example.util.ResourceAPI.saveChatRoomHistory;



public class Word{
    private static Map<String, String> wordList = new HashMap<>();
    private int randNum = 0;
    private String randomWord;
    private String randomDefinition;


    public Map<String, String> setWordList(){ //words along with their definition put into a map
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
    public int generateNumber(int count){//generates a random number depending on how many is in the word map
        Random random = new Random();
        randNum = random.nextInt(count+1);
        return randNum;
    }

    public String generateWord(Map<String, String> currentList, int randomNum){//gets a word based on the random number
        int count = 0;
        for(Entry<String, String> word: currentList.entrySet()){
            if(count == randomNum){
               randomWord = word.getKey();
            }
            count++;
        }
        return randomWord;
    }

    public String generateDefinition(Map<String, String> currentList, int randomNum){//gets a definition based on the random number
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