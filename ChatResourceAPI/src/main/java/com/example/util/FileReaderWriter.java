package com.example.util;

import org.json.JSONObject;

import java.io.*;
import java.util.Map;

public class FileReaderWriter {
    static public void saveNewFile(File dir, String name, String content) throws FileNotFoundException {
        File myFile = null;
        try {
            myFile = new File(dir, name);
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getPath());
            } else {
                System.out.println("File already exists. " + myFile.getPath());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (myFile!=null){
            PrintWriter output = new PrintWriter(myFile);
            output.print(content);
            output.close();
        }

    }

    /**
     * Method reads the content of the file and returns it as String
     * ***/
    static public String readHistoryFile(File dir, String name) throws FileNotFoundException {
        File myFile = null;
        try {
            myFile = new File(dir, name);
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getPath());
                return null;
            } else {
                System.out.println("File already exists. " + myFile.getPath());

                FileReader fileInput = new FileReader(myFile);
                BufferedReader input = new BufferedReader(fileInput);

                // read the content
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = input.readLine()) != null) {
                    buffer.append(line);
                }
                String content = buffer.toString();

                System.out.println(content);

                return content;

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}
