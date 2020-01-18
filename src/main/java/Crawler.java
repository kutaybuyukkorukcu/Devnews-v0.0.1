import model.Data;
import model.Like;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Validator;

import javax.print.Doc;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.*;

public class Crawler {

    Validator validator = new Validator();

    public Like castToLike(String url) {
        Like like = new Like();

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String topic = doc.select("div.article__category > a[title]").text();
        String mainTopic = validator.validate(topic);

        String title = doc.select("div.actions__left > h1").text();

        like.setMainTopic(mainTopic);
        like.setTitle(title);
        like.setIsNew(1);

        return like;
    }

    public void writeLikes(Like like) {

        Path path = Paths.get("src/main/resources/likes.csv");

        StringBuilder sb = new StringBuilder();
        sb.append(like.getTitle() + "\t");
        sb.append(like.getMainTopic());

        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // castToPojo(ArrayList<String> articleLinks, int articleID)
    public Data castToPojo(String url, int articleID) {

        Data data = new Data();

//        DB'den urlleri cek. Her seferinde url adinda bir String'e atansin.
//        isNew'i 1 olanlari cek sadece.
//        String url = "https://www.infoq.com/news/2019/12/oracle-goolge-api-battle/";

//        Validator validator = new Validator();

        StringBuilder topics = new StringBuilder();

        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        // articleLink yine DB'den geliyor.
        data.setArticleID(articleID);
        data.setArticleLink(url);
        data.setAuthor(author);
        data.setTitle(title);
        data.setMainTopic(mainTopic);
        data.setRelatedTopics(relatedTopics);
        data.setIsNew(1);

        return data;
    }

    public void CSVWriter(Data data) {
        Path path = Paths.get("src/main/resources/articles.csv");
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toString(data.getArticleID()) + "\t");
        sb.append(data.getTitle() + "\t");
        sb.append(data.getMainTopic() + "\t");
        sb.append(data.getAuthor() + "\t");
        sb.append(data.getRelatedTopics());

        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
            writer.newLine();
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
