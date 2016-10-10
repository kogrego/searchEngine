package il.ac.shenkar.searchengine.storage;

import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Indexer {

    private Map<String, ArrayList<String>> index;
    private Parser parser;
    private File indexFile;

    public Indexer(){
        index = new HashMap<>();
        indexFile = Utils.getIndex();
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = new FileInputStream(indexFile.getName());
            ois = new ObjectInputStream(fis);
            index = (Map<String, ArrayList<String>>)ois.readObject();
            fis.close();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        parser = new Parser();
    }

    public void add(File toAdd){
        ArrayList<String> parsedFile;
        parsedFile = parser.parse(toAdd);
        for(String word: parsedFile){
            ArrayList<String> fileList = null;
            if(index != null) {
                fileList = index.get(word);
            }
            if(fileList == null){
                fileList = new ArrayList<>();
            }
            fileList.add(toAdd.getName());
            index.put(word, fileList);
        }
        try {
//                PrintWriter writer = new PrintWriter(indexFile);
//                writer.print("");
//                writer.close();
            FileOutputStream fos = new FileOutputStream(indexFile.getName());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(index);
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(String fileName){
        if (index == null){
            throw new IllegalStateException("index is empty");
        }
        index.forEach((key, value) -> value.forEach((val)-> {
            if(val.equals(fileName)) {
                this.remove(val);
            }
        }));
    }
}
