package il.ac.shenkar.searchengine.engine;

import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Search {

    private Map<String, Map<String, Hits>> indexMap;

    public Search() {
        this.indexMap = Utils.getMap();
    }

    private ArrayList<String> parse(String[] words) throws IllegalStateException{
        if(indexMap == null){
            throw new IllegalStateException("Index file empty");
        }
        ArrayList<String> results = new ArrayList<>();
        int i = 0;
        String tempWord;
        while(i < words.length){
            switch (words[i]){
                case "NOT":
                    if(words[i+1].startsWith("(")){
                        ArrayList<String> newWords = new ArrayList<>();
                        while(!words[i+1].endsWith(")")){
                            String temp = words[i+1].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i+1].replaceAll("[()]", ""));
                        results.removeAll(parse(newWords.toArray(new String[newWords.size()])));
                        i++;
                    }
                    else if(words[i+1].startsWith("\"")){
                        String temp = "";
                        while(!words[i+1].endsWith("\"")){
                            temp += words[i+1];
                            temp += " ";
                            i++;
                        }
                        temp += words[i+1];
                        temp = temp.toLowerCase();
                        Map<String, Hits> tempNot = indexMap.get(temp);
                        if(tempNot != null) {
                            results.removeAll(tempNot.keySet());
                        }
                        i++;
                    }
                    else {
                        tempWord = words[i+1].toLowerCase();
                        Map<String, Hits> tempNot = indexMap.get(tempWord);
                        if(tempNot != null) {
                            results.removeAll(tempNot.keySet());
                        }
                        i += 2;
                    }
                    break;
                case "AND":
                    if(words[i+1].startsWith("(")){
                        ArrayList<String> newWords = new ArrayList<>();
                        while(!words[i+1].endsWith(")")){
                            String temp = words[i+1].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i+1].replaceAll("[()]", ""));
                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> tempList = parse(newWords.toArray(new String[newWords.size()]));
                        list.addAll(tempList.stream().filter(tempList::contains).collect(Collectors.toList()));
                        results.clear();
                        results.addAll(list);
                        i++;
                    }
                    else if(words[i+1].startsWith("\"")){
                        String temp = "";
                        while(!words[i+1].endsWith("\"")){
                            temp += words[i+1];
                            temp += " ";
                            i++;
                        }
                        temp += words[i+1];
                        temp = temp.toLowerCase();
                        Map<String, Hits> tempAnd = indexMap.get(temp);
                        if(tempAnd != null) {
                            ArrayList<String> list = results.stream().filter(t -> tempAnd.keySet().contains(t)).collect(Collectors.toCollection(ArrayList::new));
                            results.clear();
                            results.addAll(list);
                        }
                        i++;
                    }
                    else {
                        tempWord = words[i + 1].toLowerCase();
                        Map<String, Hits> tempAnd = indexMap.get(tempWord);
                        if(tempAnd != null) {
                            ArrayList<String> list = results.stream().filter(t -> tempAnd.keySet().contains(t)).collect(Collectors.toCollection(ArrayList::new));
                            results.clear();
                            results.addAll(list);
                        }
                        i += 2;
                    }
                    break;
                case "OR":
                    if(words[i+1].startsWith("(")){
                        ArrayList<String> newWords = new ArrayList<>();
                        while(!words[i+1].endsWith(")")){
                            String temp = words[i+1].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i+1].replaceAll("[()]", ""));
                        ArrayList<String> tempList = parse(newWords.toArray(new String[newWords.size()]));
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.clear();
                        results.addAll(set);
                        i++;
                    }
                    else if(words[i+1].startsWith("\"")){
                        String temp = "";
                        while(!words[i+1].endsWith("\"")){
                            temp += words[i+1];
                            temp += " ";
                            i++;
                        }
                        temp += words[i+1];
                        temp = temp.toLowerCase();
                        Map<String, Hits> tempOr = indexMap.get(temp);
                        if (tempOr != null) {
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            set.addAll(indexMap.get(temp).keySet());
                            results.clear();
                            results.addAll(set);
                        }
                        i++;
                    }
                    else {
                        tempWord = words[i + 1].toLowerCase();
                        Map<String, Hits> tempOr = indexMap.get(tempWord);
                        if (tempOr != null) {
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            set.addAll(indexMap.get(tempWord).keySet());
                            results.clear();
                            results.addAll(set);
                        }
                        i += 2;
                    }
                    break;
                default:
                    if(words[i].startsWith("(")){
                        ArrayList<String> newWords = new ArrayList<>();
                        while(!words[i].endsWith(")")){
                            String temp = words[i].replaceAll("[()]", "");
                            newWords.add(temp);
                            i++;
                        }
                        newWords.add(words[i].replaceAll("[()]", ""));
                        ArrayList<String> tempList = parse(newWords.toArray(new String[newWords.size()]));
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.clear();
                        results.addAll(set);
                        i++;
                    }
                    else if(words[i].startsWith("\"")){
                        String temp = "";
                        while(!words[i].endsWith("\"")){
                            temp += words[i];
                            temp += " ";
                            i++;
                        }
                        temp += words[i];
                        temp = temp.toLowerCase();
                        Map<String, Hits> tempDefault = indexMap.get(temp);
                        if(tempDefault != null) {
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            set.addAll(indexMap.get(temp).keySet());
                            results.clear();
                            results.addAll(set);
                        }
                        i++;
                    }
                    else {
                        tempWord = words[i].toLowerCase();
                        Map<String, Hits> tempDefault = indexMap.get(tempWord);
                        if(tempDefault != null) {
                            Set<String> set = new HashSet<>();
                            set.addAll(results);
                            set.addAll(indexMap.get(tempWord).keySet());
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

    public ArrayList<String> search(String term) {
        String[] words = term.split(" ");
        return parse(words);
    }

    public ArrayList<String> showDocument(ArrayList<String> documents){
        return null;
    }

}
