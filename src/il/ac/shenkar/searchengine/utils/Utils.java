package il.ac.shenkar.searchengine.utils;

import java.io.*;

public class Utils {
    private static final String[] blackList = {"on", "ON", "On", "in", "IN", "In", "to", "TO", "To",
                                                "A", "a", "an", "AN", "An", "the", "The", "THE", ""};
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

    public static void storeFile(File src, File dest) throws IOException {
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }
}