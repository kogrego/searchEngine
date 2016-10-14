package il.ac.shenkar.searchengine.storage;

import il.ac.shenkar.searchengine.utils.Hits;

import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Map<String, Hits>> indexMap;
    private Parser parser;
    private File indexFile;

    public Indexer(){
        indexMap = new HashMap<>();
//        indexFile = Utils.getIndex();
//        FileInputStream fis;
//        ObjectInputStream ois;
//        fis = new FileInputStream(indexFile.getName());
//        ois = new ObjectInputStream(fis);
//        indexMap = (Map<String, PostingData>)ois.readObject();
//        fis.close();
//        ois.close();
        parser = new Parser();
    }

    public void index(File toAdd) throws IOException {
        ArrayList<String> wordsFound = parser.parse(toAdd);
        ArrayList<String> goodWords = parser.blackList(wordsFound);
        goodWords.forEach((word)-> {
            if(indexMap.containsKey(word)) {
                Map<String, Hits> hits = indexMap.get(word);
                if(indexMap.get(word).containsKey(toAdd.getName())) {
                    Hits hit = hits.get(toAdd.getName());
                    hit.setNumOfHits(hit.getNumOfHits() + 1);
                    hits.put(toAdd.getName(), hit);
                }
                else {
                    Hits hit = new Hits(1, true);
                    hits.put(toAdd.getName(), hit);
                }
                indexMap.put(word, hits);
            }
            else {
                Map<String, Hits> firstHit = new HashMap<>();
                Hits hit = new Hits(1, true);
                firstHit.put(toAdd.getName(), hit);
                indexMap.put(word, firstHit);
            }
        });


        System.out.println("done");

//        String serial = String.valueOf(System.currentTimeMillis());
//        File storedFile = new File("./storage/" + toAdd.getName() + serial + ".txt");
//        String header = "# MetaData\n #" + "Serial: " + serial + "\n";
//        InputStream is = new FileInputStream(toAdd);
//        OutputStream os = new FileOutputStream(storedFile);
//        byte[] buffer = new byte[1024];
//        int length;
//        while((length = is.read(buffer)) > 0) {
//            os.write(buffer, 0, length);
//        }
//        is.close();
//        os.close();

//
//        for(PostingData ps: parsedFile){
//            PostingData fileList = null;
//            if(indexMap != null) {
//                fileList = indexMap.get(ps.getWord());
//            }
//            if(fileList == null){
//                fileList = new PostingData();
//                fileList.setWord(ps.getWord());
//                fileList.setNumOfFiles(ps.getHits().size());
//                fileList.setHits(ps.getHits());
//            }
//            else{
//                fileList.setNumOfFiles(ps.getNumOfFiles() + 1);
//                Map<String, Integer> hits = ps.getHits();
//                fileList.getHits().put(ps.getWord() , hits.get(ps.getWord()));
//            }
//
//            indexMap.put(ps.getWord(), fileList);
//        }
////        PrintWriter writer = new PrintWriter(indexFile);
////        writer.print("");
////        writer.close();
//        FileOutputStream fos = new FileOutputStream(indexFile.getName());
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(indexMap);
//        fos.close();
//        oos.close();
    }

    public void remove(String fileName){
//        if (indexMap == null){
//            throw new IllegalStateException("index is empty");
//        }
//        indexMap.forEach((word, pd) -> {
//            pd.getHits().forEach((key, val)->{
//                if(key.equals(fileName)){
//                    pd.setValid(false);
//                    pd.setNumOfFiles(pd.getNumOfFiles() - 1);
//                }
//            });
//        });
    }
}
