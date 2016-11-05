package il.ac.shenkar.searchengine.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Utils {
    private static final String[] blackList = {"on", "in", "to", "a", "an", "the", "i", "is", "it", "as",
            "was", "so", "his", "has", "of", ""};
    private static final String INDEX = "./index.json";
    private static final String DOCUMENTS = "./documents.json";
    private static File indexFile;
    private static File docsFile;
    private static Map<String, Hits> indexMap;
    private static Map<String, Doc> docsMap;

    private static String[] getBlackList() {
        return blackList;
    }

    public static void getIndexFile() {
        if (indexFile == null) {
            indexFile = new File(INDEX);
        }
    }

    public static void getDocumentsFile() {
        if (docsFile == null) {
            docsFile = new File(DOCUMENTS);
        }
    }

    public static Map<String, Hits> getIndexMap() {
        if (indexMap == null) {
            indexMap = new HashMap<>();
        }
        return indexMap;
    }

    public static Map<String, Doc> getDocsMap() {
        if (docsMap == null) {
            docsMap = new HashMap<>();
        }
        return docsMap;
    }

    public static ArrayList<String> getStorageFileNames() {
        ArrayList<String> fileNames = new ArrayList<>();
        docsMap.forEach((serial, doc) -> {
            fileNames.add(doc.getFileName());
        });
        return fileNames;
    }

    public static File storeFile(File src, Doc doc) throws IOException {
        doc.setSerial();
        if (docsMap == null) {
            docsMap = new HashMap<>();
        }
        docsMap.put(doc.getSerial(), doc);
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
        String mapString = gson.toJson(indexMap);
        BufferedWriter indexOut = new BufferedWriter(new FileWriter(INDEX));
        indexOut.write(mapString);
        indexOut.close();
    }

    public static void getMapFromFile() throws IOException, ClassNotFoundException {
        String mapJson = null;
        Gson gson = new Gson();
        if (indexFile.exists()) {
            mapJson = new Scanner(indexFile).useDelimiter("\\Z").next();
        } else {
            File indexFile = new File(INDEX);
            if (indexFile.exists()) {
                mapJson = new Scanner(indexFile).useDelimiter("\\Z").next();
            }
        }
        Type mapType = new TypeToken<Map<String, Hits>>() {
        }.getType();
        indexMap = gson.fromJson(mapJson, mapType);
    }

    public static void saveDocsToFile() throws IOException {
        Gson gson = new Gson();
        String mapString = gson.toJson(docsMap);
        BufferedWriter indexOut = new BufferedWriter(new FileWriter(DOCUMENTS));
        indexOut.write(mapString);
        indexOut.close();
    }

    public static void getDocsFromFile() throws IOException, ClassNotFoundException {
        String mapJson = null;
        Gson gson = new Gson();
        if (docsFile.exists()) {
            mapJson = new Scanner(docsFile).useDelimiter("\\Z").next();
        } else {
            File postingFile = new File(DOCUMENTS);
            if (postingFile.exists()) {
                mapJson = new Scanner(postingFile).useDelimiter("\\Z").next();
            }
        }
        Type mapType = new TypeToken<Map<String, Doc>>() {
        }.getType();
        docsMap = gson.fromJson(mapJson, mapType);
    }

    public static ArrayList<String> blackList(ArrayList<String> words) {
        ArrayList<String> goodWords = new ArrayList<>(words);
        List<String> blacklist = Arrays.asList(Utils.getBlackList());
        for (String word : words) {
            if (blacklist.contains(word)) {
                goodWords.remove(word);
                if (!Objects.equals(word, "")) {
                    String newWord = '"' + word + '"';
                    goodWords.add(newWord);
                }
            }
        }
        return goodWords;
    }

}