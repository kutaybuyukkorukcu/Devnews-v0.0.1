package service;

import domain.Article;
import domain.Like;
import domain.Url;
import helper.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.ArticleRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CrawlerServiceTest {
    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    CrawlerService crawlerService;

//    // Not sure about this one.
//    @Test
//    public void test_getArticleLinksFromFileAsList_whenFileDoesntExist() {
//
//        doThrow(new IOException())
//                .doNothing()
//                .when(crawlerService).getArticleLinksFromFileAsList();
//
//    }
//
//    // except empty list
//    @Test
//    public void test_getArticleLinksFromFileAsList_whenFileDataIsNotPresent() {
//
//    }
//
//    @Test
//    public void test_getArticleLinksFromFileAsList_whenFileDataIsPresent() {
//
//        List<String> urlList = crawlerService.getArticleLinksFromFileAsList();
//
//
//    }

    @Test
    public void test_articleLinkToUrl_whenArticleLinkIsNotPresent() {

        String articleLink = "";

        when(articleRepository.findByArticleLink(articleLink)).thenReturn(null);

        Like like = crawlerService.articleLinkToLike(articleLink).get();

        assertThat(like).isEqualTo(Optional.empty());

        verify(articleRepository).findByArticleLink(articleLink);
        verifyNoMoreInteractions(crawlerService);
    }

    @Test
    public void test_articleLinkToUrl_whenArticleLinkIsPresent() {
        String articleLink = "www.infoq.com/Whats-new-with-Java-11";

        Article article = new Article(1,"Whats new with Java 11", "Development", "Author",
                "Development|Java", "www.infoq.com/Whats-new-with-Java-11", true);


        when(articleRepository.findByArticleLink(articleLink)).thenReturn(article);

        Like like = crawlerService.articleLinkToLike(articleLink).get();

        assertThat(like.getTitle()).isEqualTo("Whats new with Java 11");
        assertThat(like.getMainTopic()).isEqualTo("Development");
        assertThat(like.getIsNew()).isEqualTo(true);

        verify(articleRepository).findByArticleLink(articleLink);
        verifyNoMoreInteractions(crawlerService);
    }
}
