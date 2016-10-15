package il.ac.shenkar.searchengine;

import il.ac.shenkar.searchengine.gui.Form;
import il.ac.shenkar.searchengine.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Seymore on 10/12/2016.
 */
public class Main {
    public static void main(String[] args) {
        Utils.getIndex();
        try {
            Utils.getMapFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new Form();
    }
}
