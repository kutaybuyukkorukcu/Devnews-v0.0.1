package service;

import domain.Like;
import domain.Url;
import exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.LikeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LikeServiceTest {

    @Mock
    LikeRepository likeRepository;

    @Mock
    UrlService urlService;

    @Mock
    CrawlerService crawlerService;

    @InjectMocks
    LikeService likeService;

    @Test
    public void test_addLike_whenNoLikesArePresent() {

    }

    @Test
    public void test_addLike_whenSingleLikeIsPresent() {

    }

    @Test
    public void test_getLikes() {
        Like like = new Like("What's new with Java 11", "Development", true);
        Like like1 = new Like("Comprehensive guide to unit testing", "Development", true);

        List<Like> likeList = new ArrayList();
        likeList.add(like);
        likeList.add(like1);

        when(likeRepository.findAllByIsNew()).thenReturn(likeList);

        List<Like> expectedLikeList = likeService.getNewLikes();

        verify(likeRepository).findAllByIsNew();

        assertThat(likeList).isEqualTo(expectedLikeList);
        verifyNoMoreInteractions();
    }

    // getArticleLinksAsList() bos donsun ve exception firlatsin
    @Test
    public void test_addLikedArticlesIntoLikeCollection_whenGetArticleLinksAsListIsNotPresent() {
        List<String> articleLinkList = new ArrayList();

        when(urlService.getArticleLinksAsList()).thenReturn(articleLinkList);

        doThrow(new ResourceNotFoundException())
                .doNothing()
                .when(likeService).addLikedArticlesIntoLikeCollection();

        verify(urlService).getArticleLinksAsList();
        verifyNoMoreInteractions();
    }

    // getArticleLinkAsList() dolu donsun, articleLinkToLike(articleLink) bos donsun ve exception firlatsin
    @Test
    public void test_addLikedArticlesIntoLikeCollection_whenArticleLinkToLikeIsNotPresent() {
        List<String> articleLinkList = new ArrayList();
        articleLinkList.add("www.infoq.com/Whats-new-with-Java-11");
        articleLinkList.add("www.dzone.com/Comprehensive-guide-to-unit-testing");

        when(urlService.getArticleLinksAsList()).thenReturn(articleLinkList);
        verify(urlService).getArticleLinksAsList();

        String articleLink = "www.infoq.com/Whats-new-with-Java-11";
        Like like = new Like();

        when(crawlerService.articleLinkToLike(articleLink).get()).thenReturn(like);

        doThrow(new ResourceNotFoundException())
                .doNothing()
                .when(likeService).addLikedArticlesIntoLikeCollection();


        verify(crawlerService).articleLinkToLike(articleLink);
        verifyNoMoreInteractions();
    }

    // her iki fonksiyon da dolu donsun ve likeRepository gerceklessin
    @Test
    public void test_addLikedArticlesIntoLikeCollection_whenAllDatasArePresent() {
        List<String> articleLinkList = new ArrayList();
        articleLinkList.add("www.infoq.com/Whats-new-with-Java-11");
        articleLinkList.add("www.dzone.com/Comprehensive-guide-to-unit-testing");

        when(urlService.getArticleLinksAsList()).thenReturn(articleLinkList);
        verify(urlService).getArticleLinksAsList();

        String articleLink = "www.infoq.com/Whats-new-with-Java-11";
        Like like = new Like("What's new with Java 11", "Development", true);

        when(crawlerService.articleLinkToLike(articleLink).get()).thenReturn(like);
        verify(crawlerService).articleLinkToLike(articleLink);

        verify(likeRepository).add(like);
        verifyNoMoreInteractions();
    }

}
