package il.ac.shenkar.searchengine.serach;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import il.ac.shenkar.searchengine.utils.Doc;
import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Utils;

public class Search {

    private Map<String, Hits> indexMap;

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
                    if (results.size() >= 0) {
                        results.addAll(new ArrayList<>(Utils.getDocsMap().keySet()));
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i + 1);
                        results.removeAll(tokenize(newWords.toArray(new String[newWords.size()]), parsedWords));
                        i += (newWords.size() + 1);
                    } else if (words[i + 1].startsWith("\"")) {
                        tempWord = apostrophes(words, i + 1);
                        results = getResults(results, tempWord, "NOT", false);
                        i += (tempWord.split(" ").length + 1);
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        results = getResults(results, tempWord, "NOT", false);
                        i += 2;
                    }
                    break;
                case "AND":
                    if (words[i + 1].equals("NOT")) {
                        i++;
                        notFlag = true;
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i + 1);
                        if (notFlag) {
                            newWords = deMorgan(newWords);
                        }
                        ArrayList<String> list = new ArrayList<>();
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        list.addAll(tempList.stream().filter(results::contains).collect(Collectors.toList()));
                        results.clear();
                        results.addAll(list);
                        i += (newWords.size() + 1);
                    } else if (words[i + 1].startsWith("\"")) {
                        tempWord = apostrophes(words, i + 1);
                        if (!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "AND", notFlag);
                        i += (tempWord.split(" ").length + 1);
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        if (!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "AND", notFlag);
                        i += 2;
                    }
                    if (notFlag) {
                        notFlag = false;
                    }
                    break;
                case "OR":
                    if (words[i + 1].equals("NOT")) {
                        i++;
                        notFlag = true;
                    }
                    if (words[i + 1].startsWith("(")) {
                        ArrayList<String> newWords = brackets(words, i + 1);
                        if (notFlag) {
                            newWords = deMorgan(newWords);
                            notFlag = false;
                        }
                        ArrayList<String> tempList = tokenize(newWords.toArray(new String[newWords.size()]), parsedWords);
                        Set<String> set = new HashSet<>();
                        set.addAll(tempList);
                        set.addAll(results);
                        results.addAll(set);
                        i += (newWords.size() + 1);
                    } else if (words[i + 1].startsWith("\"")) {
                        tempWord = apostrophes(words, i + 1);
                        if (!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "OR", notFlag);
                        i += (tempWord.split(" ").length + 1);
                    } else {
                        tempWord = words[i + 1].toLowerCase();
                        if (!notFlag) {
                            parsedWords.add(tempWord);
                        }
                        results = getResults(results, tempWord, "OR", notFlag);
                        i += 2;
                    }
                    if (notFlag) {
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
                        i += newWords.size();
                    } else if (words[i].startsWith("\"")) {
                        tempWord = apostrophes(words, i);
                        parsedWords.add(tempWord);
                        results = getResults(results, tempWord, "OR", false);
                        i += tempWord.split(" ").length;
                    } else {
                        tempWord = words[i].toLowerCase();
                        parsedWords.add(tempWord);
                        results = getResults(results, tempWord, "OR", false);
                        i++;
                    }
                    break;
            }
        }
        ArrayList<String> removedResults = new ArrayList<>(results);
        for (String result : results) {
            Doc doc = Utils.getDocsMap().get(result);
            if(doc != null) {
                if (doc.isHidden()) {
                    removedResults.remove(result);
                }
            }
        }
        return new ArrayList<>(removedResults);
    }

    private ArrayList<String> brackets(String[] words, int i) {
        ArrayList<String> newWords = new ArrayList<>();
        Stack<Character> bracket = new Stack<>();
        bracket.push('(');
        newWords.add(words[i].replace("(", ""));
        i++;
        while (bracket.size() > 0) {
            String temp = words[i];
            if (temp.startsWith("(")) {
                bracket.push('(');
            }
            if (temp.endsWith(")")) {
                if (bracket.size() == 1) {
                    temp = temp.replace(")", "");
                }
                bracket.pop();
            }
            newWords.add(temp);
            i++;
        }
        return newWords;
    }

    private String apostrophes(String[] words, int i) {
        String temp = "";
        while (!words[i].endsWith("\"")) {
            temp += words[i];
            temp += " ";
            i++;
        }
        if (temp.equals("")) {
            temp += words[i];
            temp = temp.toLowerCase();
            return temp;
        }
        temp += words[i];
        temp = temp.toLowerCase();
        temp = temp.replaceAll("\"", "");
        return temp;
    }

    private ArrayList<String> deMorgan(ArrayList<String> newWords) {
        int i = 0;
        String[] words = newWords.toArray(new String[newWords.size()]);
        ArrayList<String> deMorgan = new ArrayList<>();
        while (i < words.length) {
            switch (words[i]) {
                case "OR":
                    deMorgan.add("AND");
                    break;
                case "NOT":
                    break;
                case "AND":
                    deMorgan.add("OR");
                    break;
                default:
                    if (i == 0 || !Objects.equals(words[i - 1], "NOT")) {
                        deMorgan.add("NOT");
                    }
                    deMorgan.add(words[i]);
                    break;
            }
            i++;
        }

        return deMorgan;
    }

    private Set<String> defaultOp(Hits map, boolean notFlag, Set<String> results) {
        if (map != null) {
            if (notFlag) {
                Set<String> tempResults = Utils.getDocsMap().keySet();
                ArrayList<String> temp = new ArrayList<>(tempResults);
                map.getPostings().forEach((key, value) -> temp.remove(key));
                results.addAll(temp);
            } else {
                Set<String> tempResults = map.getPostings().keySet();
                ArrayList<String> temp = new ArrayList<>(tempResults);
                results.addAll(temp);
            }
        } else {
            if (notFlag) {
                results.addAll(new ArrayList<>(Utils.getDocsMap().keySet()));
            }
        }
        return results;
    }

    private Set<String> getResults(Set<String> results, String searchTerm, String op, boolean notFlag) {
        Hits map = indexMap.get(searchTerm);
        switch (op) {
            case "NOT":
                if (map != null) {
                    Set<String> finalResults = results;
                    map.getPostings().forEach((key, value) -> finalResults.remove(key));
                    results = finalResults;
                }
                break;
            case "AND":
                if (map != null) {
                    if (notFlag) {
                        ArrayList<String> tempResults = new ArrayList<>();
                        tempResults.addAll(new ArrayList<>(Utils.getStorageFileNames()));
                        Set<String> finalResults = results;
                        map.getPostings().forEach((key, value) -> finalResults.remove(key));
                        results = finalResults;
                        ArrayList<String> list = tempResults.stream().filter(results::contains).collect(Collectors.toCollection(ArrayList::new));
                        results.clear();
                        results.addAll(list);
                        break;
                    }
                    ArrayList<String> list = results.stream().filter(s -> map.getPostings().keySet().contains(s)).collect(Collectors.toCollection(ArrayList::new));
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

    public Search() {
        this.indexMap = Utils.getIndexMap();
    }

    public ArrayList<String> search(String term, ArrayList<String> parsedWords) {
        term = term.replace("(", " (");
        term = term.replace(")", ") ");
        term = term.replace("  ", " ");
        String[] words = term.split(" ");
        return tokenize(words, parsedWords);
    }

    public Doc showDocument(Doc doc) throws IOException {
        String sCurrentLine;
        String content = "";
        String serial = "";
        String filename = "./storage/" + doc.getFileName();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int i = 0;
        while ((sCurrentLine = br.readLine()) != null) {
            if (i == 2) {
                serial = sCurrentLine.split(" ")[2];
            }
            if (!sCurrentLine.startsWith("#")) {
                content += sCurrentLine;
                content += "\n";
            }
            i++;
        }
        doc.setContent(content);
        return doc;
    }

}
