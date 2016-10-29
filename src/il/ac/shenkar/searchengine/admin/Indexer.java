package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;
import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Map<String, ArrayList>> indexMap;
    private Map<String, Doc> postingMap;
    private Parser parser;

    public Indexer(){
        indexMap = Utils.getMap();
        postingMap = Utils.getPostingMap();
        parser = new Parser();
    }

    public void index(File toAdd, Doc doc) throws IOException {
        ArrayList<String> wordsFound = parser.parse(toAdd);
        ArrayList<String> goodWords = parser.blackList(wordsFound);
        goodWords.forEach((word)-> {
            if(indexMap.containsKey(word)) {
                Map<String, ArrayList> hit = indexMap.get(word);
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

//    public void hide(String fileName){
//        if (indexMap == null){
//            throw new IllegalStateException("index is empty");
//        }
//        indexMap.forEach((word, data) -> {
//            Hits hit = data.get(fileName);
//            if(hit != null){
//                hit.setValid(false);
//                data.put(fileName, hit);
//            }
//        });
//    }
//
//    public void show(String fileName){
//        if (indexMap == null){
//            throw new IllegalStateException("index is empty");
//        }
//        indexMap.forEach((word, data) -> {
//            Hits hit = data.get(fileName);
//            if(hit != null){
//                hit.setValid(true);
//                data.put(fileName, hit);
//            }
//        });
//    }


    public void addOccurrence(String fileId, Map<String, ArrayList> hit) {
        ArrayList occur = hit.get(fileId);
        occur.add(1);
        hit.put(fileId, occur);
    }

    public void addHit(String fileId, Map<String, ArrayList> hit) {
        ArrayList occur = new ArrayList();
        occur.add(1);
        hit.put(fileId, occur);
    }

    private void indexWord(String fileId, String word) {
        Map<String, ArrayList> firstHit = new HashMap<>();
        ArrayList occur = new ArrayList();
        occur.add(1);
        firstHit.put(fileId, occur);
        indexMap.put(word, firstHit);
    }
}
