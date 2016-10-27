package il.ac.shenkar.searchengine;

import il.ac.shenkar.searchengine.gui.Form;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Utils.getIndex();
        Utils.getStoredFilesList();
        try {
            Utils.getMapFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new Form();
    }
}
