package service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import model.Data;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

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

    public ArrayList<Data> getData(MongoDatabase database) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

        ArrayList<Data> list = new ArrayList<Data>();

        try(MongoCursor<Data> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Data data = cursor.next();
                list.add(data);
            }
        }

        return list;
    }

    public boolean dataExist(Data data, MongoDatabase database) {
        MongoCollection<Data> collection = database.getCollection("data", Data.class);

//        Bson queryFilter = eq("title", data.getTitle());
        Document queryFilter =  new Document("title", data.getTitle());

        FindIterable result = collection.find(queryFilter).limit(1);

        return result.first() == null ? false : true;
    }
}
