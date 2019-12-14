public class Data {

    private int id;
    private int articleID;
    private String title;
    private String mainTopic;
    private String author;
    private String relatedTopics;
    private String articleLink;

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public Data() {
    }

    public Data(int id, int articleID, String title, String mainTopic, String author, String relatedTopics, String articleLink) {
        this.id = id;
        this.articleID = articleID;
        this.title = title;
        this.mainTopic = mainTopic;
        this.author = author;
        this.relatedTopics = relatedTopics;
        this.articleLink = articleLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
