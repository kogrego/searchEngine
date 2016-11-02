package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;
import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Map<String, ArrayList<Integer>>> indexMap;
    private Map<String, Doc> postingMap;
    private Parser parser;

    public Indexer(){
        indexMap = Utils.getMap();
        postingMap = Utils.getPostingMap();
        parser = new Parser();
    }

    public void index(File toAdd, Doc doc) throws IOException {
        ArrayList<String> wordsFound = parser.parse(toAdd, doc);
        ArrayList<String> goodWords = parser.blackList(wordsFound);
        goodWords.forEach((word)-> {
            if(indexMap.containsKey(word)) {
                Map<String, ArrayList<Integer>> hit = indexMap.get(word);
                if(hit.containsKey(doc.getSerial())) {
                    addOccurrence(doc.getSerial(), hit);
                }
                else {
                    addHit(doc.getSerial(), hit);
                }
                indexMap.put(word, hit);
            }
            else {
                indexWord(doc.getSerial(), word);
            }
        });
    }


    private void addOccurrence(String fileId, Map<String, ArrayList<Integer>> hit) {
        ArrayList<Integer> occur = hit.get(fileId);
        occur.add(1);
        hit.put(fileId, occur);
    }

    private void addHit(String fileId, Map<String, ArrayList<Integer>> hit) {
        ArrayList<Integer> occur = new ArrayList<>();
        occur.add(1);
        hit.put(fileId, occur);
    }

    private void indexWord(String fileId, String word) {
        Map<String, ArrayList<Integer>> firstHit = new HashMap<>();
        ArrayList<Integer> occur = new ArrayList<>();
        occur.add(1);
        firstHit.put(fileId, occur);
        indexMap.put(word, firstHit);
    }
}
