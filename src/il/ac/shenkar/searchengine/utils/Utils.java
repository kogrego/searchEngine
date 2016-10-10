package il.ac.shenkar.searchengine.utils;

import java.io.File;

public class Utils {
    private static final String[] blackList = {"on", "ON", "On", "in", "IN", "In", "to", "TO", "To",
                                                "A", "a", "an", "AN", "An", "the", "The", "THE"};
    private static File index;
    private final String INDEX = "index.txt";

    public static String[] getBlackList() {
        return blackList;
    }

    public static File getIndex() {
        if(index == null){
            index = new File("./storage/index.txt");
        }
        return index;
    }
}