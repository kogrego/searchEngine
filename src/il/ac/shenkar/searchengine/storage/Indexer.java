package il.ac.shenkar.searchengine.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Indexer {

    private Map<String, ArrayList<String>> index;
    private Parser parser;

    public Indexer(){
        index = new HashMap<>();
        parser = new Parser();
    }

    public boolean add(File toAdd){
        ArrayList<String> parsedFile = new ArrayList<>();
        parsedFile = parser.parse(toAdd);
        for(String word: parsedFile){
            ArrayList<String> fileList;
            fileList = index.get(word);
            if(fileList == null){
                fileList = new ArrayList<>();
            }
            fileList.add(toAdd.getName());
            index.put(word, fileList);
        }
        return true;
    }

    public boolean remove(String fileName){
        return true;
    }
}
