package il.ac.shenkar.searchengine.serach;

import java.io.IOException;
import il.ac.shenkar.searchengine.serach.gui.Form;
import il.ac.shenkar.searchengine.utils.Utils;

public class Main {
    public static void main(String[] args) {
        Utils.getIndex();
        Utils.getDocuments();
        try {
            Utils.getMapFromFile();
            Utils.getDocsFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new Form();
    }
}
