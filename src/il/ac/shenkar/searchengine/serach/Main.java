package il.ac.shenkar.searchengine.serach;

import java.io.IOException;

import il.ac.shenkar.searchengine.serach.gui.SearchForm;
import il.ac.shenkar.searchengine.utils.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.getIndexFile();
        Utils.getDocumentsFile();
        try {
            Utils.getMapFromFile();
            Utils.getDocsFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new SearchForm();
    }
}
