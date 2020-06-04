package service;

import domain.Url;
import repository.UrlRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UrlService {

    protected final UrlRepository urlRepository;

    public UrlService() {
        urlRepository = new UrlRepository();
    }

    public void addUrl(Url url) {
        urlRepository.add(url);
    }

    public List<Url> getUrls() {
        List<Url> urlList = urlRepository.findAll();

        if (urlList == null) {
            return Collections.emptyList();
        }

        return urlList;
    }

    /*
    NewUrls means article urls user liked in a 1 week span
    */
    public List<String> getNewUrlsAsString() {
        List<Url> urlList = urlRepository.findAllByIsNew();

        if (urlList == null) {
            return Collections.emptyList();
        }

        List<String> stringUrlList = new ArrayList<>();

        for (Url url: urlList) {
            stringUrlList.add(url.getLink());
        }

        return stringUrlList;
    }
}
