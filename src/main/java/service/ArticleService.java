package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.Mongo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import model.Article;
import model.Data;
import org.bson.Document;
import org.bson.json.JsonReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ArticleService {

    public void JsonObjectToList(JsonObject jsonObject, ArrayList<Article> articles) {

        JsonArray jsonArray =  jsonObject.getAsJsonArray("list");
        Iterator<JsonElement> iter = jsonArray.iterator();


        while(iter.hasNext()) {
                Article article = new Article();
                JsonArray arr = (JsonArray) iter.next();
                article.setArticleID(arr.get(0).getAsInt() + 1); // Because recom.py subtracts 1 from articleID
                article.setSimilarityScore(arr.get(1).getAsDouble());
                articles.add(article);
            }

    }

    public JsonObject getRecommendations(String title) {

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:5000/api/recommend")
                    .queryString("title", title)
                    .asJson();

            JsonNode jsonNode = jsonResponse.getBody();
            JSONObject jsonObject = jsonNode.getObject();
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            return gsonObject;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new JsonObject();
    }

    public ArrayList<Article> returnRecommendations(ArrayList<Article> articles) {

        Comparator<Article> comparator = new Comparator<Article>() {
            @Override
            public int compare(Article i1, Article i2) {
                int a1 = (int) Math.round(i1.getSimilarityScore());
                int a2 = (int) Math.round(i2.getSimilarityScore());
                return a2 - a1;
            }
        };

        // Set kontrolu yapilsin. Ayni articleID'ye sahipler alinmasin.
        return (ArrayList<Article>) articles.stream()
                .sorted(comparator)
                .limit(5)
                .collect(Collectors.toList());
    }

    // Dosyaya yazma islemi ayri bir fonksiyon ve generic yapilabilir.
//    public void cekVeYaz(ArrayList<Article> articles, MongoDatabase database) {
//
//        Iterator<Article> iter = articles.iterator();
//
//        while(iter.hasNext()) {
//            int articleID = iter.next().getArticleID();
//
//            MongoCollection<Data> collection = database.getCollection("data", Data.class);
//
//            Document queryFilter =  new Document("articleID", articleID);
//
//            FindIterable<Data> result = collection.find(queryFilter).limit(1);
//
//            Data data = result.first();
//            Path path = Paths.get("src/main/resources/recommendations.txt");
//
//            StringBuilder sb = new StringBuilder();
//            sb.append(data.getMainTopic() + "\t");
//            sb.append(data.getTitle() + "\t");
//            sb.append(data.getArticleLink());
//
//            // StandardOpenOption'a CREATE_NEW ekle.
//            // Set kontrolu yapilsin. Ayni articleID'ye sahipler yazilmasin.
//            try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.APPEND)) {
//                writer.newLine();
//                writer.write(sb.toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
