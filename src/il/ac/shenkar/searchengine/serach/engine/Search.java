package il.ac.shenkar.searchengine.serach.engine;

import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Search {

    private Map<String, Map<String, Hits>> indexMap;
    private ArrayList<String> storageFileNames;

    public Search() {
        this.indexMap = Utils.getMap();
    }

    private ArrayList<String> tokenize(String[] words, ArrayList<String> parsedWords) throws IllegalStateException {
        if (indexMap == null) {
            throw new IllegalStateException("Index file empty");
        }
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        boolean notFlag = false;
        String tempWord;
        while (i < words.length) {
            switch (words[i]) {
                case "NOT":
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = new ArrayList<>();
                        while (!words[i + 1].endsWith(")")) {
                            String temp = words[i + 1].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i + 1].replaceAll("[()]", ""));
                        results.removeAll(tokenize(newWords.toArray(new String[newWords.size()]), parsedWords));
                        i+=2;
                    } else if (words[i + 1].startsWith("\"")) {
                        String temp = "";
                        while (!words[i + 1].endsWith("\"")) {
                            temp += words[i + 1];
                            temp += " ";
                            i++;
                        }
                        temp += words[i + 1];
                        temp = temp.toLowerCase();
                        temp = temp.replaceAll("\"", "");
                        parsedWords.add(temp);
                        Map<String, Hits> tempNot = indexMap.get(temp);
                        if (tempNot != null) {
                            tempNot.forEach((key, value)->{
                                if(value.isValid()){
                                    results.remove(key);
                                }
                            });
                        }
                        i++;
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, Hits> tempNot = indexMap.get(tempWord);
                        if (tempNot != null) {
                            tempNot.forEach((key, value)->{
                                if(value.isValid()){
                                    results.remove(key);
                                }
                            });
                        }
                        i += 2;
                    }
                    break;
                case "AND":
                    if(words[i + 1].equals("NOT")){
                        i++;
                        notFlag = true;
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = new ArrayList<>();
                        while (!words[i + 1].endsWith(")")) {
                            String temp = words[i + 1].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i + 1].replaceAll("[()]", ""));
                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        list.addAll(tempList.stream().filter(tempList::contains).collect(Collectors.toList()));
                        results.clear();
                        results.addAll(list);
                        i+=2;
                    } else if (words[i + 1].startsWith("\"")) {
                        String temp = "";
                        while (!words[i + 1].endsWith("\"")) {
                            temp += words[i + 1];
                            temp += " ";
                            i++;
                        }
                        temp += words[i + 1];
                        temp = temp.toLowerCase();
                        temp = temp.replaceAll("\"", "");
                        parsedWords.add(temp);
                        Map<String, Hits> tempAnd = indexMap.get(temp);
                        if (tempAnd != null) {
                            if(notFlag) {
                                tempAnd.forEach((key, value) -> {
                                    if (value.isValid()) {
                                        results.remove(key);
                                    }
                                });
                                notFlag = false;
                            }
                            ArrayList<String> list = results.stream().filter(s -> tempAnd.keySet().contains(s) && tempAnd.get(s).isValid()).collect(Collectors.toCollection(ArrayList::new));
                            results.clear();
                            results.addAll(list);
                        }
                        i++;
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, Hits> tempAnd = indexMap.get(tempWord);
                        if (tempAnd != null) {
                            if(notFlag) {
                                tempAnd.forEach((key, value) -> {
                                    if (value.isValid()) {
                                        results.remove(key);
                                    }
                                });
                                notFlag = false;
                            }
                            ArrayList<String> list = results.stream().filter(s -> tempAnd.keySet().contains(s) && tempAnd.get(s).isValid()).collect(Collectors.toCollection(ArrayList::new));
                            results.clear();
                            results.addAll(list);
                        }
                        i += 2;
                    }
                    break;
                case "OR":
                    if(words[i + 1].equals("NOT")){
                        i++;
                        notFlag = true;
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = new ArrayList<>();
                        while (!words[i + 1].endsWith(")")) {
                            String temp = words[i + 1].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i + 1].replaceAll("[()]", ""));
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.clear();
                        results.addAll(set);
                        i+=2;
                    } else if (words[i + 1].startsWith("\"")) {
                        String temp = "";
                        while (!words[i + 1].endsWith("\"")) {
                            temp += words[i + 1];
                            temp += " ";
                            i++;
                        }
                        temp += words[i + 1];
                        temp = temp.toLowerCase();
                        temp = temp.replaceAll("\"", "");
                        parsedWords.add(temp);
                        Map<String, Hits> tempOr = indexMap.get(temp);
                        if (tempOr != null) {
                            if(notFlag) {
                                tempOr.forEach((key, value) -> {
                                    if (value.isValid()) {
                                        results.remove(key);
                                    }
                                });
                                notFlag = false;
                            }
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempOr.forEach((key,value)->{
                                if(value.isValid()){
                                    set.add(key);
                                }
                            });
                            results.clear();
                            results.addAll(set);
                        }
                        i++;
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, Hits> tempOr = indexMap.get(tempWord);
                        if (tempOr != null) {
                            if(notFlag) {
                                tempOr.forEach((key, value) -> {
                                    if (value.isValid()) {
                                        results.remove(key);
                                    }
                                });
                                notFlag = false;
                            }
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempOr.forEach((key,value)->{
                                if(value.isValid()){
                                    set.add(key);
                                }
                            });
                            results.clear();
                            results.addAll(set);
                        } else {
                            results.addAll(Utils.getStorageFileNames());
                        }
                        i += 2;
                    }
                    break;
                default:
                    if (words[i].startsWith("(")) {
                        ArrayList<String> newWords = new ArrayList<>();
                        while (!words[i].endsWith(")")) {
                            String temp = words[i].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i].replaceAll("[()]", ""));
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.clear();
                        results.addAll(set);
                        i+=2;
                    } else if (words[i].startsWith("\"")) {
                        String temp = "";
                        while (!words[i].endsWith("\"")) {
                            temp += words[i];
                            temp += " ";
                            i++;
                        }
                        temp += words[i];
                        temp = temp.toLowerCase();
                        temp = temp.replaceAll("\"", "");
                        parsedWords.add(temp);
                        Map<String, Hits> tempDefault = indexMap.get(temp);
                        if (tempDefault != null) {
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempDefault.forEach((key,value)->{
                                if(value.isValid()){
                                    set.add(key);
                                }
                            });
                            results.clear();
                            results.addAll(set);
                        }
                        i++;
                    } else {
                        tempWord = words[i].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, Hits> tempDefault = indexMap.get(tempWord);
                        if (tempDefault != null) {
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempDefault.forEach((key,value)->{
                                if(value.isValid()){
                                    set.add(key);
                                }
                            });
                            results.clear();
                            results.addAll(set);
                        }
                        i++;
                    }
                    break;
            }
        }
        return results;
    }

    public ArrayList<String> search(String term, ArrayList<String> parsedWords) {
        term = term.replace("(", " (");
        term = term.replace(")", ") ");
        term = term.replace("  ", " ");
        String[] words = term.split(" ");
        return tokenize(words, parsedWords);
    }

    public Map<String, Map<String,ArrayList<Integer>>> showDocument(String filename, ArrayList<String> searchWords) throws IOException {
        Map<String, Map<String,ArrayList<Integer>>> returnDoc = new HashMap<>();
        String sCurrentLine;
        String doc = "";
        filename = "./storage/" + filename;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while ((sCurrentLine = br.readLine()) != null) {
            if(!sCurrentLine.startsWith("#")) {
                doc += sCurrentLine;
                doc += "\n";
            }
        }
        String lowerDoc = doc.toLowerCase();
        lowerDoc = lowerDoc.replaceAll("[\\-+\\^:,;?!.()/]", " ");
        br.close();
        Map<String, ArrayList<Integer>> wordData = new HashMap<>();
        for(String word: searchWords){
            int lastIndex = 0;
            ArrayList<Integer> wordLoc = new ArrayList<>();
            while (lastIndex != -1) {
                lastIndex = lowerDoc.indexOf(word, lastIndex);
                if (lastIndex != -1) {
                    String sub = lowerDoc.substring(lastIndex);
                    String[] parsedSub = sub.split(" ");
                    if(parsedSub[0].equals(word)) {
                        if (lastIndex == 0 || lowerDoc.charAt(lastIndex - 1) == ' ' || lowerDoc.charAt(lastIndex - 1) == '\n') {
                            wordLoc.add(lastIndex);
                        }
                    }
                    lastIndex += word.length();
                }
            }
            wordData.put(word, wordLoc);
        }
        returnDoc.put(doc, wordData);
        return returnDoc;
    }

}