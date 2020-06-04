package domain;

public class Url {

    private String link;
    private int isNew;

    public Url() {

    }

    public Url(String link, int isNew) {
        this.link = link;
        this.isNew = isNew;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
