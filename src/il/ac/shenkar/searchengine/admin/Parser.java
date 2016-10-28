package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


class Parser {

    ArrayList<String> parse(File toParse) throws FileNotFoundException {
        Scanner sc = new Scanner(toParse);
        ArrayList<String> words = new ArrayList<>();
        String text = "";
        String parsedLine;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if(!line.startsWith("#")) {
                text += line;
                text += " ";
            }
        }
        text = text.toLowerCase();
        text = text.replace("\n", " ");
        String[] lines = text.split("\"");
        for(int i = 0; i < lines.length; i++) {
            if((lines.length > 1) && (i % 2 != 0)) {
                words.add(lines[i]);
            }
            lines[i] = lines[i].replaceAll("[\\-+\\^:,;?!.()/]"," ");
            parsedLine = lines[i].replaceAll("  "," ");
            if(parsedLine == null || parsedLine.isEmpty()) {
                continue;
            }
            parsedLine = parsedLine.replace("  ", " ");
            Collections.addAll(words, parsedLine.split(" "));
        }
        return words;
    }

    ArrayList<String> blackList(ArrayList<String> words) {
        ArrayList<String> unique = new ArrayList<>(words);
        List<String> blacklist =  Arrays.asList(Utils.getBlackList());
        unique.removeAll(blacklist);
        return unique;
    }
}