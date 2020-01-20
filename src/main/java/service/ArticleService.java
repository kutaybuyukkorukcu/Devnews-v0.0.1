package service;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mongodb.util.JSON;
import model.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ArticleService {

    public void JSONArrayToList(JSONArray array) {

        Iterator iter = array.iterator();

        ArrayList<Article> articles = new ArrayList<>();
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

    public JSONArray getRecommendations() {

        try {
            JsonNode title = Unirest.get("http://localhost:5000/api/recommend")
                    .queryString("title", "Performance is a Key .NET Core Feature")
                    .asJson()
                    .getBody();

            JSONArray array = title.getArray();

            JSONObject jsonObject = array.getJSONObject(0);
            JSONArray arr =  jsonObject.getJSONArray("list");

            return arr;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new JSONArray();
    }
}
