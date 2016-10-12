package il.ac.shenkar.searchengine.utils;

import java.io.File;

public class Utils {
    private static final String[] blackList = {"on", "ON", "On", "in", "IN", "In", "to", "TO", "To",
                                                "A", "a", "an", "AN", "An", "the", "The", "THE"};
    private static File index;
    private static File posting;
    private static final String INDEX = "./index.txt";
    private static final String POSTING = "./posting.txt";

    public static String[] getBlackList() {
        return blackList;
    }

    public static File getIndex() {
        if(index == null){
            index = new File(INDEX);
        }
        return index;
    }

    public static File getPosting() {
        if(posting == null){
            posting = new File(POSTING);
        }
        return posting;
    }
}