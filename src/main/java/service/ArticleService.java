package service;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.util.JSON;
import model.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ArticleService {

    public void JSONArrayToList(JSONArray array, ArrayList<Article> articles) {

        Iterator iter = array.iterator();

        Article article = new Article();

        if (articles.isEmpty()) {

            while(iter.hasNext()) {
                JSONArray arr = (JSONArray) iter.next();
                article.setArticleID(arr.optInt(0));
                article.setSimilarityScore(arr.optDouble(1));
                articles.add(article);
            }
        }

    }

    public JSONArray getRecommendations(String title) {

        try {
            JsonNode jsonNode = Unirest.get("http://localhost:5000/api/recommend")
                    .queryString("title", title)
                    .asJson()
                    .getBody();

            JSONArray jsonArray = jsonNode.getArray();
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            return jsonObject.getJSONArray("list");
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new JSONArray();
    }

    public void returnRecommendations(ArrayList<Article> articles) {

//        Iterator<Article> iter = articles.iterator();
//
//        while(iter.hasNext()) {
//            iter.next()
//        }

        Comparator<Article> comparator = new Comparator<Article>() {
            @Override
            public int compare(Article i1, Article i2) {
                int a1 = (int) Math.round(i1.getSimilarityScore());
                int a2 = (int) Math.round(i2.getSimilarityScore());
                return (a1 - a2);
            }
        };

        // Eger comparator olmaz ise Article'a direk Comparable<Article> implement edip,
        // compareTo'yu implement edebilirim.
        articles = (ArrayList<Article>) articles.stream().sorted(comparator).limit(5);
        // her main_topic'e ozgu article bu fonksiyona verilir ve benzerlik orani en fazla olan ilk 5 makale alinir.
        // Burdan donen listelerde bulunan articleID'lere 1 eklenir ve Data collectionundan bu articlelarin title'lari ve url'leri cekilir.
        // Cekilen title ve url'de recommendations.txt'ye yazilir.
    }
}
