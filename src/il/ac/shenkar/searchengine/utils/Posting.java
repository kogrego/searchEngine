package il.ac.shenkar.searchengine.utils;

import java.util.ArrayList;

/**
 * Created by Seymore on 11/4/2016.
 */
public class Posting {
    private int hits;
    ArrayList<Integer> occurences;

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public ArrayList<Integer> getOccurences() {
        return occurences;
    }

    public void setOccurences(ArrayList<Integer> occurences) {
        this.occurences = occurences;
    }
}
