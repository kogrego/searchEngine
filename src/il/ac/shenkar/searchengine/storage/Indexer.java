package il.ac.shenkar.searchengine.storage;

import il.ac.shenkar.searchengine.utils.PostingData;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, PostingData> index;
    private Parser parser;
    private File indexFile;

    public Indexer() throws IOException, ClassNotFoundException {
        index = new HashMap<>();
        indexFile = Utils.getIndex();
        FileInputStream fis;
        ObjectInputStream ois;
        fis = new FileInputStream(indexFile.getName());
        ois = new ObjectInputStream(fis);
        index = (Map<String, PostingData>)ois.readObject();
        fis.close();
        ois.close();
        parser = new Parser();
    }

    public void add(File toAdd) throws IOException {
        ArrayList<PostingData> parsedFile = parser.parse(toAdd);
        String serial = String.valueOf(System.currentTimeMillis());
        File storedFile = new File("./storage/files/" + toAdd.getName() + serial + ".txt");
        Scanner sc = new Scanner(toAdd.getName());
        String fileData = "";
        while (sc.hasNextLine()){
            fileData += sc.nextLine() + "\n";
        }
        String header = "# MetaData\n #" + "Serial: " + serial + "\n";
        PrintWriter pw = new PrintWriter(storedFile.getName());
        pw.println(header);
        pw.println(fileData);
        pw.close();

        for(PostingData ps: parsedFile){
            PostingData fileList = null;
            if(index != null) {
                fileList = index.get(ps.getWord());
            }
            if(fileList == null){
                fileList = new PostingData();
                fileList.setWord(ps.getWord());
                fileList.setNumOfFiles(ps.getHits().size());
                fileList.setHits(ps.getHits());
            }
            else{
                fileList.setNumOfFiles(ps.getNumOfFiles() + 1);
                Map<String, Integer> hits = ps.getHits();
                fileList.getHits().put(ps.getWord() , hits.get(ps.getWord()));
            }

            index.put(ps.getWord(), fileList);
        }
//        PrintWriter writer = new PrintWriter(indexFile);
//        writer.print("");
//        writer.close();
        FileOutputStream fos = new FileOutputStream(indexFile.getName());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(index);
        fos.close();
        oos.close();
    }

    public void remove(String fileName){
        if (index == null){
            throw new IllegalStateException("index is empty");
        }
        index.forEach((k, v) -> {
            v.getHits().forEach((key, val)->{
                if(key.equals(fileName)){
                    v.setValid(false);
                    v.setNumOfFiles(v.getNumOfFiles() - 1);
                }
            });
        });
    }
}
