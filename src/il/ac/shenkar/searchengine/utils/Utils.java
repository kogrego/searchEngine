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
    private static File storageFile;
    private static Map<String, Map<String, Hits>> map;
    private static final String INDEX = "./index.txt";
    private static ArrayList<String> storageFileNames;
    private static final String STORAGE_FILE_NAME = "./storage.txt";

    public static String[] getBlackList() {
        return blackList;
    }

    public static void getIndex() {
        if(index == null){
            index = new File(INDEX);
        }
    }

    public static void getStoredFilesList() {
        if(storageFile == null){
            storageFile = new File(STORAGE_FILE_NAME);
        }
    }

    public static Map<String, Map<String, Hits>> getMap() {
        if(map == null){
            map = new HashMap<>();
        }
        return map;
    }

    public static ArrayList<String> getStorageFileNames(){
        if(storageFileNames == null){
            storageFileNames = new ArrayList<>();
        }
        return storageFileNames;
    }

    public static File storeFile(File src) throws IOException {
        String serial = String.valueOf(System.currentTimeMillis());
        String newFileName =  serial + ".txt";
        if(storageFileNames == null){
            storageFileNames = new ArrayList<>();
        }
        storageFileNames.add(src.getName());
        File dest = new File("./storage/" + newFileName);
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        String head = "# MetaData \n# original file name: " + src.getName() + "\n" ;
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

    public static void saveMapToFile() throws IOException {
        Gson gson = new Gson();
        String mapString = gson.toJson(map);
        BufferedWriter indexOut = new BufferedWriter(new FileWriter(INDEX));
        indexOut.write(mapString);
        indexOut.close();
        String fileNames = gson.toJson(storageFileNames);
        BufferedWriter namesOut = new BufferedWriter(new FileWriter(STORAGE_FILE_NAME));
        namesOut.write(fileNames);
        namesOut.close();
    }

    public static void getMapFromFile() throws IOException, ClassNotFoundException {
        String mapJson;
        Gson gson = new Gson();
        if(index.exists()) {
            mapJson = new Scanner(index).useDelimiter("\\Z").next();
        }
        else {
            mapJson = new Scanner(new File(INDEX)).useDelimiter("\\Z").next();
        }
        Type mapType = new TypeToken<Map<String, Map<String, Hits>>>(){}.getType();
        map = gson.fromJson(mapJson, mapType);
        String namesJson = new Scanner(new File(STORAGE_FILE_NAME)).useDelimiter("\\Z").next();
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        storageFileNames = gson.fromJson(namesJson, listType);
    }

}