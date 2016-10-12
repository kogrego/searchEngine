package il.ac.shenkar.searchengine.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Seymore on 10/12/2016.
 */
public class PostingData implements Serializable{
    private String word;
    private int numOfFiles;
    private Map<String, Integer> hits;
    private boolean valid;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumOfFiles() {
        return numOfFiles;
    }

    public void setNumOfFiles(int numOfFiles) {
        this.numOfFiles = numOfFiles;
    }

    public Map<String, Integer> getHits() {
        return hits;
    }

    public void setHits(Map<String, Integer> hits) {
        this.hits = hits;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "PostingData{" +
                "word='" + word + '\'' +
                ", numOfFiles=" + numOfFiles +
                ", hits=" + hits.toString() +
                ", valid=" + valid +
                '}';
    }
}

