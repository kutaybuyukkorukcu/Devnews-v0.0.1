package model;

public class Like {

    private String link;

    private String title;

    private String mainTopic;
    private int isNew;
    public Like() {

    }

    public Like(String link, String title, String mainTopic, int isNew) {
        this.link = link;
        this.title = title;
        this.mainTopic = mainTopic;
        this.isNew = isNew;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainTopic() {
        return mainTopic;
    }

    public void setMainTopic(String mainTopic) {
        this.mainTopic = mainTopic;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
