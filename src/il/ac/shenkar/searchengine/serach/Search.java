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
                        ArrayList<String> newWords = brackets(words, i+1);
                        results.removeAll(tokenize(newWords.toArray(new String[newWords.size()]), parsedWords));
                        i+=(newWords.size()+1);
                    } else if (words[i + 1].startsWith("\"")) {
                        tempWord = apostrophes(words, i+1);
                        parsedWords.add(tempWord);
                        results = getResults(results, tempWord, "NOT", notFlag);
                        i+=(tempWord.split(" ").length + 1);
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        parsedWords.add(tempWord);
                        results = getResults(results, tempWord, "NOT", notFlag);
                        i += 2;
                    }
                    break;
                case "AND":
                    if(words[i + 1].equals("NOT")){
                        i++;
                        notFlag = true;
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i+1);
                        if(notFlag){
                            newWords = deMorgan(newWords);
                        }
                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        list.addAll(tempList.stream().filter(tempList::contains).collect(Collectors.toList()));
                        results.addAll(list);
                        i+=(newWords.size()+1);
                    } else if (words[i + 1].startsWith("\"")) {
                        tempWord = apostrophes(words, i+1);
                        if(!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "AND", notFlag);
                        i+=(tempWord.split(" ").length + 1);
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        if(!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "AND", notFlag);
                        i += 2;
                    }
                    if(notFlag) {
                        notFlag = false;
                    }
                    break;
                case "OR":
                    if(words[i + 1].equals("NOT")){
                        i++;
                        notFlag = true;
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i+1);
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
                        tempWord = apostrophes(words, i+1);
                        if(!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "OR", notFlag);
                        i+=(tempWord.split(" ").length + 1);
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        if(!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "OR", notFlag);
                        i += 2;
                    }
                    if(notFlag){
                        notFlag = false;
                    }
                    break;
                default:
                    if (words[i].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i);
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.addAll(set);
                        i+=newWords.size();
                    } else if (words[i].startsWith("\"")) {
                        tempWord = apostrophes(words, i);
                        parsedWords.add(tempWord);
                        results = getResults(results, tempWord, "OR", false);
                        i+=tempWord.split(" ").length;
                    } else {
                        tempWord = words[i].toLowerCase();
                        parsedWords.add(tempWord);
                        results = getResults(results, tempWord, "OR", false);
                        i++;
                    }
                    break;
            }
        }
        for(String result: results){
            if(Utils.getPostingMap().get(result).isHidden()){
                results.remove(result);
            }
        }
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
        newWords.add(words[i].replace("(", ""));
        i++;
        while (bracket.size() > 0){
            String temp = words[i];
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

    private String apostrophes(String[] words, int i){
        String temp = "";
        while (!words[i].endsWith("\"")) {
            temp += words[i];
            temp += " ";
            i++;
        }
        temp += words[i];
        temp = temp.toLowerCase();
        temp = temp.replaceAll("\"", "");
        return temp;
    }

    private Set<String> getResults(Set<String> results, String searchTerm, String op, boolean notFlag){
        Map<String, ArrayList<Integer>> map = indexMap.get(searchTerm);
        switch(op){
            case "NOT":
                if (map != null) {
                    Set<String> finalResults = results;
                    map.forEach((key, value)-> finalResults.remove(key));
                    results = finalResults;
                }
                break;
            case "AND":
                if (map != null) {
                    if(notFlag) {
                        ArrayList<String> tempResults = new ArrayList<>();
                        tempResults.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                        Set<String> finalResults = results;
                        map.forEach((key, value) -> finalResults.remove(key));
                        results = finalResults;
                        ArrayList<String> list = tempResults.stream().filter(results::contains).collect(Collectors.toCollection(ArrayList::new));
                        results.clear();
                        results.addAll(list);
                        break;
                    }
                    ArrayList<String> list = results.stream().filter(s -> map.keySet().contains(s)).collect(Collectors.toCollection(ArrayList::new));
                    results.clear();
                    results.addAll(list);
                } else {
                    results.clear();
                }
                break;
            case "OR":
                results = defaultOp(map, notFlag, results);
                break;
            default:
                results = defaultOp(map, notFlag, results);
                break;
        }
        return results;
    }

    private Set<String> defaultOp(Map<String, ArrayList<Integer>> map, boolean notFlag, Set<String> results) {
        if (map != null) {
            if (notFlag) {
                Set<String> tempResults = Utils.getPostingMap().keySet();
                ArrayList<String> temp = new ArrayList<>(tempResults);
                map.forEach((key, value) -> temp.remove(key));
                results.addAll(temp);
            } else {
                results.addAll(map.keySet());
            }

        } else {
            results.addAll(new ArrayList<>(Utils.getStorageFileNames()));
        }
        return results;
    }
}
