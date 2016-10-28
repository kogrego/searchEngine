package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Map<String, Hits>> indexMap;
    private Parser parser;

    public Indexer(){
        indexMap = Utils.getMap();
        parser = new Parser();
    }

    public void index(File toAdd) throws IOException {
        ArrayList<String> wordsFound = parser.parse(toAdd);
        ArrayList<String> goodWords = parser.blackList(wordsFound);
        goodWords.forEach((word)-> {
            if(indexMap.containsKey(word)) {
                Map<String, Hits> hits = indexMap.get(word);
                if(hits.containsKey(toAdd.getName())) {
                    addOccurence(toAdd, hits);
                }
                else {
                    addHit(toAdd, hits);
                }
                indexMap.put(word, hits);
            }
            else {
                indexWord(toAdd, word);
            }
        });
    }

    public void hide(String fileName){
        if (indexMap == null){
            throw new IllegalStateException("index is empty");
        }
        indexMap.forEach((word, data) -> {
            Hits hit = data.get(fileName);
            if(hit != null){
                hit.setValid(false);
                data.put(fileName, hit);
            }
        });
    }

    public void show(String fileName){
        if (indexMap == null){
            throw new IllegalStateException("index is empty");
        }
        indexMap.forEach((word, data) -> {
            Hits hit = data.get(fileName);
            if(hit != null){
                hit.setValid(true);
                data.put(fileName, hit);
            }
        });
    }

    //Todo: should be a method of hits

    public void addOccurence(File toAdd, Map<String, Hits> hits) {
        Hits hit = hits.get(toAdd.getName());
        hit.setNumOfHits(hit.getNumOfHits() + 1);
        hits.put(toAdd.getName(), hit);
    }

    //Todo: should be a method of hits

    public void addHit(File toAdd, Map<String, Hits> hits) {
        Hits hit = new Hits(1, true);
        hits.put(toAdd.getName(), hit);
    }

    private void indexWord(File toAdd, String word) {
        Map<String, Hits> firstHit = new HashMap<>();
        Hits hit = new Hits(1, true);
        firstHit.put(toAdd.getName(), hit);
        indexMap.put(word, firstHit);
    }
}
