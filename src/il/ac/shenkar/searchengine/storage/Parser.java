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
            parsedLine = parsedLine.replace("  ", "");
            if(parsedLine == null || parsedLine.isEmpty()) {
                continue;
            }
            Collections.addAll(words, parsedLine.split(" "));
        }
        return words;
    }

    public ArrayList<String> blackList(ArrayList<String> words) {
        ArrayList<String> unique = new ArrayList<>(words);
        List<String> blacklist =  Arrays.asList(Utils.getBlackList());
        unique.removeAll(blacklist);
        return unique;
    }

    public Set<Object> findDuplicates(ArrayList<String> list) {
        Set<Object> items = new HashSet<Object>();
        Set<Object> duplicates = new HashSet<Object>();
        for (Object item : list) {
            if (items.contains(item)) {
                duplicates.add(item);
            } else {
                items.add(item);
            }
        }
        return duplicates;
    }
}