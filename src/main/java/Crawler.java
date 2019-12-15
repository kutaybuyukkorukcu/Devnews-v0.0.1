import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Validator;

import java.io.IOException;

public class Crawler {

    // Get articleLink's from database. List or not i will decide that when i think of multithreading and concurreny.
    
    public DataDTO fill() {

        DataDTO dataDTO = new DataDTO();

        // DB'den urlleri cek. Her seferinde url adinda bir String'e atansin.
        String url = "https://www.infoq.com/news/2019/12/oracle-goolge-api-battle/";

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

        // DB'de id ve articleID kendisi articak sekilde yaratilacak.
        // articleLink yine DB'den geliyor.
        dataDTO.setArticleLink(url);
        dataDTO.setAuthor(author);
        dataDTO.setTitle(title);
        dataDTO.setMainTopic(mainTopic);
        dataDTO.setRelatedTopics(relatedTopics);

        // DB'ye tekrar yazma.

        return dataDTO;
    }
}
