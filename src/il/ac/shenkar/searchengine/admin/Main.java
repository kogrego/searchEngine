package il.ac.shenkar.searchengine.admin;

import java.io.IOException;

import il.ac.shenkar.searchengine.admin.gui.AdminForm;
import il.ac.shenkar.searchengine.utils.Utils;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        Utils.getIndexFile();
        Utils.getDocumentsFile();
        try {
            Utils.getMapFromFile();
            Utils.getDocsFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new AdminForm();
    }
}