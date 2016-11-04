package il.ac.shenkar.searchengine.admin;

import java.io.IOException;

import il.ac.shenkar.searchengine.admin.gui.Admin;
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
        new Admin();
    }
}