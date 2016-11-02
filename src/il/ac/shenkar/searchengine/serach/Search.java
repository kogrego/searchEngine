package il.ac.shenkar.searchengine.serach;

import il.ac.shenkar.searchengine.utils.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Search {

    private Map<String, Map<String, ArrayList<Integer>>> indexMap;

    public Search() {
        this.indexMap = Utils.getMap();
    }

    private ArrayList<String> tokenize(String[] words, ArrayList<String> parsedWords) throws IllegalStateException {
        if (indexMap == null) {
            throw new IllegalStateException("Index file empty");
        }
        Set<String> results = new HashSet<>();
        int i = 0;
        boolean notFlag = false;
        String tempWord;
        while (i < words.length) {
            switch (words[i]) {
                case "NOT":
                    if(results.size() >= 0){
                        results.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i);
                        results.removeAll(tokenize(newWords.toArray(new String[newWords.size()]), parsedWords));
                        i+=(newWords.size()+1);
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
                        Map<String, ArrayList<Integer>> tempNot = indexMap.get(temp);
                        if (tempNot != null) {
                            tempNot.forEach((key, value)-> results.remove(key));
                        }
                        i++;
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, ArrayList<Integer>> tempNot = indexMap.get(tempWord);
                        if (tempNot != null) {
                            tempNot.forEach((key, value)-> results.remove(key));
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
                        ArrayList<String> newWords = brackets(words, i);
                        if(notFlag){
                            newWords = deMorgan(newWords);
                            notFlag = false;
                        }
                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        list.addAll(tempList.stream().filter(tempList::contains).collect(Collectors.toList()));
                        results.addAll(list);
                        i+=(newWords.size()+1);
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
                        Map<String, ArrayList<Integer>> tempAnd = indexMap.get(temp);
                        if (tempAnd != null) {
                            if(notFlag) {
                                ArrayList<String> tempResults = new ArrayList<>();
                                tempResults.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                                tempAnd.forEach((key, value) -> results.remove(key));
                                notFlag = false;
                                ArrayList<String> list = tempResults.stream().filter(results::contains).collect(Collectors.toCollection(ArrayList::new));
                                results.clear();
                                results.addAll(list);
                                i++;
                                break;
                            }
                            ArrayList<String> list = results.stream().filter(s -> tempAnd.keySet().contains(s)).collect(Collectors.toCollection(ArrayList::new));
                            results.clear();
                            results.addAll(list);
                        } else {
                            results.clear();
                        }
                        i++;
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, ArrayList<Integer>> tempAnd = indexMap.get(tempWord);
                        if (tempAnd != null) {
                            if(notFlag) {
                                ArrayList<String> tempResults = new ArrayList<>();
                                tempResults.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                                tempAnd.forEach((key, value) -> tempResults.remove(key));
                                notFlag = false;
                                ArrayList<String> list = tempResults.stream().filter(results::contains).collect(Collectors.toCollection(ArrayList::new));
                                results.clear();
                                results.addAll(list);
                                i += 2;
                                break;
                            }
                            ArrayList<String> list = results.stream().filter(s -> tempAnd.keySet().contains(s)).collect(Collectors.toCollection(ArrayList::new));
                            results.clear();
                            results.addAll(list);
                        } else {
                            results.clear();
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
                        ArrayList<String> newWords = brackets(words, i);
                        if(notFlag){
                            newWords = deMorgan(newWords);
                            notFlag = false;
                        }
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.addAll(set);
                        i+=(newWords.size()+1);
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
                        Map<String, ArrayList<Integer>> tempOr = indexMap.get(temp);
                        if (tempOr != null) {
                            if(notFlag) {
                                tempOr.forEach((key, value) -> results.remove(key));
                                notFlag = false;
                            }
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempOr.forEach((key,value)-> set.add(key));
                            results.addAll(set);
                        } else {
                            results.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                        }
                        i++;
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, ArrayList<Integer>> tempOr = indexMap.get(tempWord);
                        if (tempOr != null) {
                            if(notFlag) {
                                tempOr.forEach((key, value) -> results.remove(key));
                                notFlag = false;
                            }
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempOr.forEach((key,value)-> set.add(key));
                            results.addAll(set);
                        } else {
                            results.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                        }
                        i += 2;
                    }
                    break;
                default:
                    if (words[i].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i);
                        if(notFlag){
                            newWords = deMorgan(newWords);
                            notFlag = false;
                        }
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.addAll(set);
                        i+=(newWords.size()+1);
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
                        Map<String, ArrayList<Integer>> tempDefault = indexMap.get(temp);
                        if (tempDefault != null) {
                            if(notFlag) {
                                tempDefault.forEach((key, value) -> results.remove(key));
                                notFlag = false;
                            }
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempDefault.forEach((key,value)-> set.add(key));
                            results.addAll(set);
                        } else {
                            results.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                        }
                        i++;
                    } else {
                        tempWord = words[i].toLowerCase();
                        parsedWords.add(tempWord);
                        Map<String, ArrayList<Integer>> tempDefault = indexMap.get(tempWord);
                        if (tempDefault != null) {
                            if(notFlag) {
                                tempDefault.forEach((key, value) -> results.remove(key));
                                notFlag = false;
                            }
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            tempDefault.forEach((key,value)-> set.add(key));
                            results.addAll(set);
                        } else {
                            results.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                        }
                        i++;
                    }
                    break;
            }
        }
        results.forEach((key)->{
            if(Utils.getPostingMap().get(key).isHidden()){
                results.remove(key);
            }
        });
        return new ArrayList<>(results);
    }

    private ArrayList<String> deMorgan(ArrayList<String> newWords) {
        int i = 0;
        String[] words = newWords.toArray(new String[newWords.size()]);
        ArrayList<String> deMorgan = new ArrayList<>();
        while (i < words.length){
            switch (words[i]){
                case "OR":
                    deMorgan.add("AND");
                    break;
                case "NOT":
                    break;
                case "AND":
                    deMorgan.add("OR");
                    break;
                default:
                    if(i == 0 || !Objects.equals(words[i - 1], "NOT")){
                        deMorgan.add("NOT");
                    }
                    deMorgan.add(words[i]);
                    break;
            }
            i++;
        }
        return deMorgan;
    }

    public ArrayList<String> search(String term, ArrayList<String> parsedWords) {
        term = term.replace("(", " (");
        term = term.replace(")", ") ");
        term = term.replace("  ", " ");
        String[] words = term.split(" ");
        return tokenize(words, parsedWords);
    }

    public AbstractMap.Entry<String, String> showDocument(String filename) throws IOException {
        String sCurrentLine;
        String doc = "";
        String serial = "";
        filename = "./storage/" + filename;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int i = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            if(i == 2){
                serial = sCurrentLine.split(" ")[2];
            }
            if(!sCurrentLine.startsWith("#")) {
                doc += sCurrentLine;
                doc += "\n";
            }
            i++;
        }
        return new AbstractMap.SimpleEntry<>(serial, doc);
    }

    private ArrayList<String> brackets(String[] words, int i) {
        ArrayList<String> newWords = new ArrayList<>();
        Stack<Character> bracket = new Stack<>();
        bracket.push('(');
        newWords.add(words[i+1].replace("(", ""));
        i++;
        while (bracket.size() > 0){
            String temp = words[i+1];
            if(temp.startsWith("(")){
                bracket.push('(');
            }
            if(temp.endsWith(")")){
                if(bracket.size() == 1){
                    temp = temp.replace(")", "");
                }
                bracket.pop();
            }
            newWords.add(temp);
            i++;
        }
        return newWords;
    }
}
