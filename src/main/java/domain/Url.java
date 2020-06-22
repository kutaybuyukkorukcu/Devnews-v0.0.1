package domain;

public class Url {

    private String articleLink;
    private boolean isNew;

    public Url() {

    }

    public Url(String articleLink, boolean isNew) {
        this.articleLink = articleLink;
        this.isNew = isNew;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
