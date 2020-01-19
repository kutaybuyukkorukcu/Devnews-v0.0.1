package model;

public class Url {

    private String url;
    private int isNew;

    public Url() {

    }

    public Url(String url, int isNew) {
        this.url = url;
        this.isNew = isNew;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
