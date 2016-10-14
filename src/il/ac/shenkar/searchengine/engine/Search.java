package il.ac.shenkar.searchengine.engine;

import il.ac.shenkar.searchengine.utils.Hits;
import il.ac.shenkar.searchengine.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

public class Search {

    private Map<String, Map<String, Hits>> indexMap;

    public Search() {
        this.indexMap = Utils.getMap();
    }

    public ArrayList<String> search(String term) {
        String[] words = term.split(" ");
        return null;
    }
}
