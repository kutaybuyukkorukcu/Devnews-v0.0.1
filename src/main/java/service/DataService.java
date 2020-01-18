package service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Data;
import org.bson.Document;

public class DataService {

    public void addData(Data data, MongoDatabase database){
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

//        Document query = new Document();

        collection.insertOne(data);

//        MongoCursor<Data> cursor = collection.find(query).iterator();
//
//
//        try {
//            while (cursor.hasNext()) {
//                System.out.println(cursor.next().toString());
//            }
//        }finally {
//            mongoClient.close();
//        }
    }
}
