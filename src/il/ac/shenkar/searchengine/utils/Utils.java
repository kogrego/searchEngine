package il.ac.shenkar.searchengine.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static final String[] blackList = {"on", "in", "to", "a", "an", "the", "i", "is", "it", "as",
                                                "was", "so", "his", "has", ""};
    private static File index;
    private static Map<String, Map<String, Hits>> map;
    private static final String INDEX = "./index.ser";

    public static String[] getBlackList() {
        return blackList;
    }

    public static File getIndex() {
        if(index == null){
            index = new File(INDEX);
        }
        return index;
    }

    public static Map<String, Map<String, Hits>> getMap() {
        if(map == null){
            map = new HashMap<>();
        }
        return map;
    }

    public static File storeFile(File src) throws IOException {
        String serial = String.valueOf(System.currentTimeMillis());
        File dest = new File("./storage/" + src.getName() + serial + ".txt");
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        String head = "# MetaData \n# Serial: " + serial + "\n";
        os.write(head.getBytes());
        byte[] buffer = new byte[1024];
        int length;
        while((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
        return dest;
    }

    public static void saveMapToFile(Map<String,Map<Integer, Hits>> map) throws IOException {
        OutputStream file = new FileOutputStream(INDEX);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(map);
        file.close();
        buffer.close();
        output.close();
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Map<Integer, Hits>> getMapFromFile() throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(INDEX);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream (buffer);
        Map<String,Map<Integer, Hits>> indexMap = (Map<String, Map<Integer, Hits>>) input.readObject();
        file.close();
        buffer.close();
        input.close();
        return indexMap;
    }

}