package il.ac.shenkar.searchengine.utils;

import java.util.ArrayList;

public class Posting {

    private int numOfHits;
    private ArrayList<Integer> hits;

    public int getNumOgHits() {
        return numOfHits;
    }

    public void setNumOfHits(int hits) {
        this.numOfHits = hits;
    }

    public ArrayList<Integer> getHits() {
        return hits;
    }

    public void setHits(ArrayList<Integer> occurrences) {
        this.hits = occurrences;
    }
}
