package service;

import domain.Like;
import exception.ResourceNotFoundException;
import repository.LikeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LikeService {

    protected final UrlService urlService;
    protected final CrawlerService crawlerService;
    protected final LikeRepository likeRepository;

    public LikeService() {
        urlService = new UrlService();
        crawlerService = new CrawlerService();
        likeRepository = new LikeRepository();
    }

    public List<Like> getLikes() {
        List<Like> likeList = likeRepository.findAll();

        if (likeList == null) {
            return Collections.emptyList();
        }

        return likeList;
    }

    /*
    NewLikes means articles user liked in a 1 week span
    */
    public List<Like> getNewLikes() {
        List<Like> likeList = likeRepository.findAllByIsNew();

        if (likeList == null) {
            return Collections.emptyList();
        }

        return likeList;
    }

    public void addLikedArticlesIntoLikeCollection() {
        List<String> articleLinkList = urlService.getArticleLinksAsList();

        if (articleLinkList.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        for (String articleLink : articleLinkList) {

            Optional<Like> like = crawlerService.articleLinkToLike(articleLink);

            if (!like.isPresent()) {
                // TODO : error/log handling
                // TODO : throw custom exception

                throw new ResourceNotFoundException();
            }

            likeRepository.add(like.get());
        }
    }
}