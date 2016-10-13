package il.ac.shenkar.searchengine.storage;

import il.ac.shenkar.searchengine.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Parser {

    public ArrayList<String> parse(File toParse) throws FileNotFoundException {
        Scanner sc = new Scanner(toParse);
        ArrayList<String> words = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String parsedLine = line.replaceAll("[^\\w\\s]", " ");
            String fixedLine = parsedLine.replace("  ", " ");
            Collections.addAll(words, fixedLine.split(" "));
        }
        return words;
    }

//    public ArrayList<String> sort(ArrayList<String> unsortedWords) {
//        //
//    }
//
    public ArrayList<String> blackList(ArrayList<String> words) {
        ArrayList<String> unique = new ArrayList<>(words);
        List<String> blacklist =  Arrays.asList(Utils.getBlackList());
        unique.removeAll(blacklist);
        return unique;
    }


}