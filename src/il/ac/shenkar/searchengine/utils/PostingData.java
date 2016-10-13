package il.ac.shenkar.searchengine.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Seymore on 10/12/2016.
 */
public class PostingData implements Serializable{
    private String word;
    private Set<Hits> hits;

    public PostingData(String word, Set<Hits> hits) {

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<Hits> getHits() {
        return hits;
    }

    public void setHits(Set<Hits> hits) {
        this.hits = hits;
    }
}

