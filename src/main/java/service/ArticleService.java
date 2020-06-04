package service;

import domain.Article;
import repository.ArticleRepository;

import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ArticleService {

    protected final ArticleRepository articleRepository;

    public ArticleService() {
        articleRepository = new ArticleRepository();
    }

    public void addArticle(Article article){
        articleRepository.add(article);
    }

    public List<Article> getArticles() {
        List<Article> articleList = articleRepository.findAll();

        if (articleList == null) {
            return Collections.emptyList();
        }

        return articleList;
    }

    public boolean doesArticleExist(Article article) {

        Article mockArticle = articleRepository.findByTitle(article.getTitle());

        if (mockArticle == null) {
            return false;
        }

        return true;
    }
}
