package il.ac.shenkar.searchengine.admin;

import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Posting;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.*;

public class Indexer {

    private Map<String, Hits> indexMap;
    private Map<String, Doc> docsMap;

    public Indexer() {
        indexMap = Utils.getIndexMap();
        docsMap = Utils.getDocsMap();
    }

    public void index(File toAdd, Doc doc) throws IOException {
        ArrayList<String> words = new ArrayList<>();
        String parsedLine;
        String text = getText(toAdd, doc);
        text = text.toLowerCase();
        text = text.replace("\n", " ");
        String[] lines = text.split("\"");
        for (int i = 0; i < lines.length; i++) {
            if ((lines.length > 1) && (i % 2 != 0)) {
                words.add(lines[i]);
            }
            lines[i] = lines[i].replaceAll("[\\-+\\^:,;?!.()/]", " ");
            parsedLine = lines[i].replaceAll("  ", " ");
            if (parsedLine == null || parsedLine.isEmpty()) {
                continue;
            }
            parsedLine = parsedLine.replace("  ", " ");
            Collections.addAll(words, parsedLine.split(" "));
        }

        ArrayList<String> uniqueWords = removeDup(words);
        ArrayList<String> goodWords = Utils.blackList(uniqueWords);

        for (String word : goodWords) {
            int lastIndex = 0;
            while (lastIndex != -1) {
                String tempWord = word;
                if (tempWord.split(" ").length == 1) {
                    tempWord = word.replaceAll("\"", "");
                }
                lastIndex = text.indexOf(tempWord, lastIndex);
                if (lastIndex != -1) {
                    String toIndex = text.substring(lastIndex, lastIndex + tempWord.length());
                    if (toIndex.equals(tempWord)) {
                        if (lastIndex == 0 || text.charAt(lastIndex - 1) == ' ' || text.charAt(lastIndex - 1) == '\n') {
                            if (indexMap.containsKey(word)) {
                                Hits hit = indexMap.get(word);
                                if (hit.getPostings().containsKey(doc.getSerial())) {
                                    addOccurrence(doc.getSerial(), hit, lastIndex);
                                } else {
                                    addHit(doc.getSerial(), hit, lastIndex);
                                }
                                indexMap.put(word, hit);
                            } else {
                                indexWord(doc.getSerial(), word, lastIndex);
                            }
                        }
                    }
                    lastIndex += word.length();
                }
            }
        }
    }


    private void addOccurrence(String fileId, Hits hit, int index) {
        ArrayList<Integer> occur = hit.getPostings().get(fileId).getHits();
        occur.add(index);
        hit.getPostings().get(fileId).setHits(occur);
        hit.getPostings().get(fileId).setNumOfHits(hit.getPostings().get(fileId).getNumOgHits() + 1);
    }

    private void addHit(String fileId, Hits hit, int index) {
        ArrayList<Integer> occur = new ArrayList<>();
        occur.add(index);
        Posting posting = new Posting();
        posting.setHits(occur);
        posting.setNumOfHits(1);
        Map<String, Posting> postingObj = hit.getPostings();
        postingObj.put(fileId, posting);
        hit.setNumOfPostings(hit.getNumOfPostings() + 1);
        hit.setPostings(postingObj);
    }

    private void indexWord(String fileId, String word, int index) {
        Hits firstHit = new Hits();
        ArrayList<Integer> occur = new ArrayList<>();
        occur.add(index);
        Posting posting = new Posting();
        posting.setHits(occur);
        posting.setNumOfHits(1);
        Map<String, Posting> postingObj = new HashMap<>();
        postingObj.put(fileId, posting);
        firstHit.setPostings(postingObj);
        firstHit.setNumOfPostings(1);
        indexMap.put(word, firstHit);
    }

    ArrayList<String> removeDup(ArrayList<String> words) {
        Set<String> unique = new HashSet<>(words);
        return new ArrayList<>(unique);
    }

    private String getText(File file, Doc doc) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String text = "";
        int i = 0;
        String preview = "";
        String title = "";
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.startsWith("#")) {
                if (i < 4) {
                    if (i == 0) {
                        title += line;
                        line = sc.nextLine();
                    } else {
                        preview += line + '\n';
                    }
                    i++;
                }
                text += line;
                text += " ";
            }
        }
        doc.setTitle(title);
        doc.setPreview(preview);
        return text;
    }
}
