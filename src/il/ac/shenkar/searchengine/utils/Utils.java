package il.ac.shenkar.searchengine.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Utils {
    private static final String[] blackList = {"on", "in", "to", "a", "an", "the", "i", "is", "it", "as",
            "was", "so", "his", "has", ""};
    private static File index;
    private static File posting;
    private static Map<String, Hits> map;
    private static Map<String, Doc> postingMap;
    private static final String INDEX = "./index.txt";
    private static final String POSTING = "./posting.txt";

    public static String[] getBlackList() {
        return blackList;
    }

    public static void getIndex() {
        if (index == null) {
            index = new File(INDEX);
        }
    }

    public static void getPosting() {
        if (posting == null) {
            posting = new File(POSTING);
        }
    }

    public static Map<String, Hits> getMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    public static Map<String, Doc> getPostingMap() {
        if (postingMap == null) {
            postingMap = new HashMap<>();
        }
        return postingMap;
    }


    public static ArrayList<String> getStorageFileNames() {
        return new ArrayList<>(postingMap.keySet());
    }

    public static File storeFile(File src, Doc doc) throws IOException {
        doc.setSerial();
        if (postingMap == null) {
            postingMap = new HashMap<>();
        }
        postingMap.put(doc.getSerial(), doc);
        File dest = new File("./storage/" + doc.getFileName());
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        String head = "# MetaData \n# original file name: " + src.getName() + "\n# serial " + doc.getSerial() + "\n";
        os.write(head.getBytes());
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
        return dest;
    }

    public static void saveMapToFile() throws IOException {
        Gson gson = new Gson();
        String mapString = gson.toJson(map);
        BufferedWriter indexOut = new BufferedWriter(new FileWriter(INDEX));
        indexOut.write(mapString);
        indexOut.close();
    }

    public static void getMapFromFile() throws IOException, ClassNotFoundException {
        String mapJson = null;
        Gson gson = new Gson();
        if (index.exists()) {
            mapJson = new Scanner(index).useDelimiter("\\Z").next();
        } else {
            File indexFile = new File(INDEX);
            if(indexFile.exists()) {
                mapJson = new Scanner(indexFile).useDelimiter("\\Z").next();
            }
        }
        Type mapType = new TypeToken<Map<String, Hits>>(){}.getType();
        map = gson.fromJson(mapJson, mapType);
    }

    public static void savePostingToFile() throws IOException {
        Gson gson = new Gson();
        String mapString = gson.toJson(postingMap);
        BufferedWriter indexOut = new BufferedWriter(new FileWriter(POSTING));
        indexOut.write(mapString);
        indexOut.close();
    }

    public static void getPostingFromFile() throws IOException, ClassNotFoundException {
        String mapJson = null;
        Gson gson = new Gson();
        if (posting.exists()) {
            mapJson = new Scanner(posting).useDelimiter("\\Z").next();
        } else {
            File postingFile = new File(POSTING);
            if(postingFile.exists()) {
                mapJson = new Scanner(postingFile).useDelimiter("\\Z").next();
            }
        }
        Type mapType = new TypeToken<Map<String, Doc>>() {
        }.getType();
        postingMap = gson.fromJson(mapJson, mapType);
    }

}