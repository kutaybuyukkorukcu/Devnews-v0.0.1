package model;

public class Data {

    private int articleID;
    private String title;

    private String mainTopic;

    private String author;
    private String relatedTopics;
    private String articleLink;

    private int isNew;

    public Data() {

    }

    public Data(int articleID, String title, String mainTopic, String author, String relatedTopics, String articleLink, int isNew) {
        this.articleID = articleID;
        this.title = title;
        this.mainTopic = mainTopic;
        this.author = author;
        this.relatedTopics = relatedTopics;
        this.articleLink = articleLink;
        this.isNew = isNew;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRelatedTopics() {
        return relatedTopics;
    }

    public void setRelatedTopics(String relatedTopics) {
        this.relatedTopics = relatedTopics;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Data{" +
                "articleID=" + articleID +
                ", title='" + title + '\'' +
                ", mainTopic='" + mainTopic + '\'' +
                ", author='" + author + '\'' +
                ", relatedTopics='" + relatedTopics + '\'' +
                ", articleLink='" + articleLink + '\'' +
                '}';
    }
}
