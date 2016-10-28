package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Map<String, Hits>> indexMap;
    private Map<String, String> fileIndexMap;
    private Parser parser;

    public Indexer(){
        indexMap = Utils.getMap();
        fileIndexMap = Utils.getStorageFileNames();
        parser = new Parser();
    }

    public void index(File toAdd) throws IOException {
        String fileId = toAdd.getName().substring(0, toAdd.getName().lastIndexOf('.'));
        ArrayList<String> wordsFound = parser.parse(toAdd);
        ArrayList<String> goodWords = parser.blackList(wordsFound);
        goodWords.forEach((word)-> {
            if(indexMap.containsKey(word)) {
                Map<String, Hits> hits = indexMap.get(word);
                if(hits.containsKey(fileId)) {
                    addOccurrence(fileId, hits);
                }
                else {
                    addHit(fileId, hits);
                }
                indexMap.put(word, hits);
            }
            else {
                indexWord(fileId, word);
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

    public void addOccurrence(String fileId, Map<String, Hits> hits) {
        Hits hit = hits.get(fileId);
        hit.setNumOfHits(hit.getNumOfHits() + 1);
        hits.put(fileId, hit);
    }

    //Todo: should be a method of hits

    public void addHit(String fileId, Map<String, Hits> hits) {
        Hits hit = new Hits(1, true);
        hits.put(fileId, hit);
    }

    private void indexWord(String fileId, String word) {
        Map<String, Hits> firstHit = new HashMap<>();
        Hits hit = new Hits(1, true);
        firstHit.put(fileId, hit);
        indexMap.put(word, firstHit);
    }
}
