package model;

import java.util.ArrayList;

public class Article {

    private int articleID;
    private double similarityScore;

    public Article() {

    }

    public Article(int articleID, double similarityScore) {
        this.articleID = articleID;
        this.similarityScore = similarityScore;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
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
                "articleID=" + articleID +
                ", similarityScore=" + similarityScore +
                '}';
    }
}
