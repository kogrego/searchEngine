package il.ac.shenkar.searchengine.utils;


public class Doc {

    private String fileName;
    private String serial;
    private boolean isHidden;

    public Doc(String fileName) {
        this.fileName = fileName;
        this.serial = String.valueOf(System.currentTimeMillis());
        this.isHidden = false;
    }

    public void show() {
        isHidden = false;
    }

    public void hide() {
        isHidden = true;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSerial() {
        return serial;
    }
}


