package il.ac.shenkar.searchengine.utils;


public class Doc {

    private String fileName;
    private String serial;
    private String title;
    private String preview;
    private String content;
    private boolean isHidden;

    public Doc(String fileName) {
        this.fileName = fileName;
        this.isHidden = false;
    }

    void setSerial() {
        this.serial = String.valueOf(System.currentTimeMillis());
    }

    public String getFileName() {
        return fileName;
    }

    public String getSerial() {
        return serial;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getContent() {
        return content;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void show() {
        isHidden = false;
    }

    public void hide() {
        isHidden = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


