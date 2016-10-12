package il.ac.shenkar.searchengine.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Parser {

    public ArrayList<String> parse(File toParse){
        Scanner sc = new Scanner(toParse.getName());
        ArrayList<String> words = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Collections.addAll(words, line.split(" "));
        }
        return null;
    }
}