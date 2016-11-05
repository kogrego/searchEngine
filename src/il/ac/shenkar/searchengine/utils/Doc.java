package il.ac.shenkar.searchengine.utils;


public class Doc {

    private String fileName;
    private String serial;
    private String preview;
    private String content;
    private boolean isHidden;

    public Doc(String fileName) {
        this.fileName = fileName;
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

    public void setSerial() {
        this.serial = String.valueOf(System.currentTimeMillis());
    }

    public boolean isHidden() {
        return isHidden;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


