package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Utils;
import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Map<String, ArrayList<Integer>>> indexMap;
    private Map<String, Doc> postingMap;

    public Indexer(){
        indexMap = Utils.getMap();
        postingMap = Utils.getPostingMap();
    }

    public void index(File toAdd, Doc doc) throws IOException {
        ArrayList<String> words = new ArrayList<>();
        String parsedLine;
        String text = getText(toAdd, doc);
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
        ArrayList<String> goodWords = blackList(words);
        ArrayList<String> uniqueWords = removeDup(goodWords);

        for(String word: uniqueWords){
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = text.indexOf(word, lastIndex);
                if (lastIndex != -1) {
                    String sub = text.substring(lastIndex);
                    String[] parsedSub = sub.split(" ");
                    if(parsedSub[0].equals(word)) {
                        if (lastIndex == 0 || text.charAt(lastIndex - 1) == ' ' || text.charAt(lastIndex - 1) == '\n') {
                            if(indexMap.containsKey(word)) {
                                Map<String, ArrayList<Integer>> hit = indexMap.get(word);
                                if(hit.containsKey(doc.getSerial())) {
                                    addOccurrence(doc.getSerial(), hit, lastIndex);
                                }
                                else {
                                    addHit(doc.getSerial(), hit, lastIndex);
                                }
                                indexMap.put(word, hit);
                            }
                            else {
                                indexWord(doc.getSerial(), word, lastIndex);
                            }
                        }
                    }
                    lastIndex += word.length();
                }
            }
        }
    }


    private void addOccurrence(String fileId, Map<String, ArrayList<Integer>> hit, int index) {
        ArrayList<Integer> occur = hit.get(fileId);
        occur.add(index);
        hit.put(fileId, occur);
    }

    private void addHit(String fileId, Map<String, ArrayList<Integer>> hit, int index) {
        ArrayList<Integer> occur = new ArrayList<>();
        occur.add(index);
        hit.put(fileId, occur);
    }

    private void indexWord(String fileId, String word, int index) {
        Map<String, ArrayList<Integer>> firstHit = new HashMap<>();
        ArrayList<Integer> occur = new ArrayList<>();
        occur.add(index);
        firstHit.put(fileId, occur);
        indexMap.put(word, firstHit);
    }

    ArrayList<String> blackList(ArrayList<String> words) {
        ArrayList<String> goodWords = new ArrayList<>(words);
        List<String> blacklist =  Arrays.asList(Utils.getBlackList());
        goodWords.removeAll(blacklist);
        return goodWords;
    }

    ArrayList<String> removeDup(ArrayList<String> words){
        Set<String> unique = new HashSet<>(words);
        return new ArrayList<>(unique);
    }

    private String getText(File file, Doc doc) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String text = "";
        int i = 0;
        String preview = "";
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if(!line.startsWith("#")) {
                if(i < 3) {
                    preview += line + '\n';
                    i++;
                }
                text += line;
                text += " ";
            }
        }
        doc.setPreview(preview);
        return text;
    }
}
