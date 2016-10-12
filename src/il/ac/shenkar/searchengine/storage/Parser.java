package il.ac.shenkar.searchengine.storage;

import il.ac.shenkar.searchengine.utils.PostingData;
import il.ac.shenkar.searchengine.utils.Utils;
import javafx.geometry.Pos;

import java.io.File;
import java.util.*;

import static il.ac.shenkar.searchengine.utils.Utils.getBlackList;

public class Parser {

    public ArrayList<PostingData> parse(File toParse){
        Scanner sc = new Scanner(toParse.getName());
        ArrayList<String> words = new ArrayList<>();
        Set<String> uniqe = new HashSet<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String parsedLine = line.replaceAll("[^\\w\\s]", " ");
            String fixedLine = parsedLine.replace("  ", " ");
            Collections.addAll(words, fixedLine.split(" "));
            Collections.addAll(uniqe, fixedLine.split(" "));
        }
        List<String> blacklist =  Arrays.asList(Utils.getBlackList());
        words.removeAll(blacklist);
        uniqe.removeAll(blacklist);
        ArrayList<PostingData> sortedWords = new ArrayList<>();
        for(String word: uniqe) {
            PostingData ps = new PostingData();
            Map<String, Integer> hits = new HashMap<>();
            hits.put(toParse.getName(), Collections.frequency(words, word));
            ps.setWord(word);
            ps.setHits(hits);
            ps.setValid(true);
            sortedWords.add(ps);
        }
        Collections.sort(sortedWords, (o1, o2) -> o1.getWord().compareTo(o2.getWord()));
        return sortedWords;
    }
}