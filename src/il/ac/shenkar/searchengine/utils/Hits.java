package il.ac.shenkar.searchengine.utils;

import java.util.Map;

public class Hits {

    private int numOfPostings;
    private Map<String, Posting> postings;

    public int getNumOfPostings() {
        return numOfPostings;
    }

    public void setNumOfPostings(int numOfPostings) {
        this.numOfPostings = numOfPostings;
    }

    public Map<String, Posting> getPostings() {
        return postings;
    }

    public void setPostings(Map<String, Posting> postings) {
        this.postings = postings;
    }
}
