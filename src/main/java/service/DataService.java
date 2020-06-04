package service;

import domain.Data;
import repository.DataRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class DataService {

    protected final DataRepository dataRepository;

    public DataService() {
        dataRepository = new DataRepository();
    }

    public void addData(Data data){
        dataRepository.add(data);
    }

    public List<Data> getDatas() {
        List<Data> dataList = dataRepository.findAll();

        if (dataList == null) {
            return Collections.emptyList();
        }

        return dataList;
    }

    public boolean doesDataExist(Data data) {

        Data mockData = dataRepository.findByTitle(data.getTitle());

        if (mockData == null) {
            return false;
        }

        return true;
    }

//    public String createMail(Data data) {
//        // verilerden mail formati olusturmasini bekleriz.
//        String mainTopic = data.getMainTopic();
//        String title = data.getTitle();
//        String url = data.getArticleLink();
//
//        String html = "<h2> " + mainTopic + " </h2>" + "\n"
//                + "<h4> " + title + " </h4>" + "\n"
//                + "<h5> " + url + " </h5>";
//
//        return html;
//    }
}
