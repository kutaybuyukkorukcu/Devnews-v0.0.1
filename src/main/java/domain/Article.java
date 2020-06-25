package domain;

public class Article {

    private int articleId;
    private String title;

    private String mainTopic;

    private String author;
    private String relatedTopics;
    private String articleLink;
    private String articleSource;

    private boolean isNew;

    public Article() {

    }

    public Article(int articleId, String title, String mainTopic, String author, String relatedTopics,
                   String articleLink, boolean isNew) {
        this.articleId = articleId;
        this.title = title;
        this.mainTopic = mainTopic;
        this.author = author;
        this.relatedTopics = relatedTopics;
        this.articleLink = articleLink;
        this.isNew = isNew;
    }

    public Article(int articleId, String title, String mainTopic, String author, String relatedTopics,
                   String articleLink, String articleSource, boolean isNew) {
        this.articleId = articleId;
        this.title = title;
        this.mainTopic = mainTopic;
        this.author = author;
        this.relatedTopics = relatedTopics;
        this.articleLink = articleLink;
        this.articleSource = articleSource;
        this.isNew = isNew;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
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

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleID=" + articleId +
                ", title='" + title + '\'' +
                ", mainTopic='" + mainTopic + '\'' +
                ", author='" + author + '\'' +
                ", relatedTopics='" + relatedTopics + '\'' +
                ", articleLink='" + articleLink + '\'' +
                '}';
    }
}
