package il.ac.shenkar.searchengine.utils;

import java.io.Serializable;

/**
 * Created by Seymore on 10/12/2016.
 */
public class Hits implements Serializable{
    private int numOfHits;
    private boolean isValid;

    public Hits(int numOfHits, boolean isValid) {
        this.numOfHits = numOfHits;
        this.isValid = isValid;
    }

    public int getNumOfHits() {
        return numOfHits;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setNumOfHits(int numOfHits) {
        this.numOfHits = numOfHits;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "Hits{" +
                " numOfHits=" + numOfHits +
                ", isValid=" + isValid +
                '}';
    }
}
