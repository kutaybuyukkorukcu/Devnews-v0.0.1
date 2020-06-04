package domain;

public class Url {

    private String articleLink;
    private int isNew;

    public Url() {

    }

    public Url(String articleLink, int isNew) {
        this.articleLink = articleLink;
        this.isNew = isNew;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
