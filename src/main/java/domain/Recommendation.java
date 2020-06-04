package domain;

public class Recommendation {

    private int articleId;
    private double similarityScore;

    public Recommendation() {

    }

    public Recommendation(int articleId, double similarityScore) {
        this.articleId = articleId;
        this.similarityScore = similarityScore;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleID=" + articleId +
                ", similarityScore=" + similarityScore +
                '}';
    }
}
