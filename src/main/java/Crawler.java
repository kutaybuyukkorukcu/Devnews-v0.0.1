import model.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Validator;

import java.io.IOException;

public class Crawler {

    // Get articleLink's from database. List or not i will decide that when i think of multithreading and concurreny.
    // Create it as a function and use it in castToPojo() function.
    // This function returns articleLinks contained in ArrayList.
    // getArticleLinks(); <- This function will be located in DataTransaction.

    // castToPojo(ArrayList<String> articleLinks, int articleID)
    public Data castToPojo(String url, int articleID) {

        Data data = new Data();

//        DB'den urlleri cek. Her seferinde url adinda bir String'e atansin.
//        isNew'i 1 olanlari cek sadece.
//        String url = "https://www.infoq.com/news/2019/12/oracle-goolge-api-battle/";

        Validator validator = new Validator();

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
}
