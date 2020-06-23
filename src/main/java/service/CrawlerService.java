package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import domain.Article;
import domain.Counter;
import domain.Like;
import domain.Url;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import helper.Validator;
import repository.ArticleRepository;
import utils.initializeDB;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CrawlerService {

    protected final Validator validator;
    protected final ArticleRepository articleRepository;

    public CrawlerService() {
        validator = new Validator();
        articleRepository = new ArticleRepository();
    }

    public Article crawlArticleLinkIntoArticle(String articleLink) throws IOException {

        Article article = new Article();

        StringBuilder topics = new StringBuilder();

        Document doc = null;

        doc = Jsoup.connect(articleLink).get();

        // TODO : logging and handling null articleLink

        String topic = doc.select("div.article__category > a[title]").text();
        String mainTopic = validator.validate(topic);

        String title = doc.select("div.actions__left > h1").text();
        String author = doc.select("span.author__name > a").text();

        Elements ul = doc.select("div.topics > ul");
        Elements li = ul.select("li");

        for(Element item : li) {
            topics.append(item.text() + "|");
        }

        String relatedTopics = validator.removeLastChar(topics.toString());

        int articleID = articleRepository.getNextArticleIdSequence();

        // articleLink yine DB'den geliyor.
        article.setArticleId(articleID);
        article.setArticleLink(articleLink);
        article.setAuthor(author);
        article.setTitle(title);
        article.setMainTopic(mainTopic);
        article.setRelatedTopics(relatedTopics);
        article.setIsNew(true);

        return article;
    }

    public void writeArticlesIntoCSV(Article article) {
        Path path = Paths.get("src/main/resources/articles.csv");

        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(article.getArticleId()) + "\t");
        sb.append(article.getTitle() + "\t");
        sb.append(article.getMainTopic() + "\t");
        sb.append(article.getAuthor() + "\t");
        sb.append(article.getRelatedTopics());

        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(sb.toString());
        } catch (IOException e) {
            // TODO : error handling - log handling
            e.printStackTrace();
        }
    }

    public List<String> getArticleLinksFromFileAsList() {

        try {
            Stream<String> stream = Files.lines(Paths.get("src/main/resources/urls.txt"));

            List<String> urlList = new ArrayList<>();

            stream.filter(s -> s.endsWith("/"))
                    .forEach(urlList::add);

            if (urlList == null) {
                return Collections.emptyList();
            }

            return urlList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    public Optional<Like> articleLinkToLike(String articleLink) {

        Article article = articleRepository.findByArticleLink(articleLink);

        if (article == null) {
            return Optional.empty();
        }

        Like like = new Like();

        like.setTitle(article.getTitle());
        like.setMainTopic(article.getMainTopic());
        like.setIsNew(true);

        return Optional.ofNullable(like);
    }

    // TODO : gerek kalmayabilir buna
//    public void writeLikes(Like like) {
//
//        Path path = Paths.get("src/main/resources/likes.csv");
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(like.getTitle() + "\t");
//        sb.append(like.getMainTopic());
//
//        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
//            writer.newLine();
//            writer.write(sb.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
