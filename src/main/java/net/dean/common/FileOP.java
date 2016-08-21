package net.dean.common;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by dean on 16/7/10.
 */
public class FileOP {

    public static void writeFile(String fileName, String appendMsg, List<?> lists) throws IOException {
        CharSink charSink = Files.asCharSink(new File(fileName), Charsets.UTF_8, FileWriteMode.APPEND);
        charSink.write(appendMsg+ "\n");
        charSink.write(lists.toString() +"\n");
    }

    public static void writeFile(String fileName, String appendMsg, Object object) throws IOException {
        CharSink charSink = Files.asCharSink(new File(fileName), Charsets.UTF_8, FileWriteMode.APPEND);
        charSink.write(appendMsg+"\n");
        charSink.write(object.toString()+"\n");
    }

    public static void writeFile(String fileName, String message){
        try {
            CharSink charSink = Files.asCharSink(new File(fileName), Charsets.UTF_8, FileWriteMode.APPEND);
            charSink.write(message + "\n");
        }catch(Exception e){

        }
    }
}
