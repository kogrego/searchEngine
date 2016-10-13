package il.ac.shenkar.searchengine.utils;

/**
 * Created by Seymore on 10/12/2016.
 */
public class Hits {
    private String fileName;
    private int numOfHits;
    private boolean isValid;

    public Hits(String fileName, int numOfHits, boolean isValid) {
        this.fileName = fileName;
        this.numOfHits = numOfHits;
        this.isValid = isValid;
    }

    public String getFileName() {
        return fileName;
    }

    public int getNumOfHits() {
        return numOfHits;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
                "fileName='" + fileName + '\'' +
                ", numOfHits=" + numOfHits +
                ", isValid=" + isValid +
                '}';
    }
}
