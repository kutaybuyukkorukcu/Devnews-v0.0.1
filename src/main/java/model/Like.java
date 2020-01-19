package model;

public class Like {

    private int articleID;
    private String url;
    private String title;
    private String mainTopic;
    private int isNew;

    public Like() {

    }

    public Like(int articleID, String url, String title, String mainTopic, int isNew) {
        this.articleID = articleID;
        this.url = url;
        this.title = title;
        this.mainTopic = mainTopic;
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Like{" +
                "articleID=" + articleID +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", mainTopic='" + mainTopic + '\'' +
                ", isNew=" + isNew +
                '}';
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
