package service;

import domain.Url;
import repository.UrlRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public List<String> getArticleLinksAsList() {
        List<Url> urlList = urlRepository.findAllByIsNew();

        if (urlList == null) {
            return Collections.emptyList();
        }

        List<String> articleLinkList = new ArrayList<>();

        for (Url url: urlList) {
            articleLinkList.add(url.getArticleLink());
        }

        return articleLinkList;
    }
}
