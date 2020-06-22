package service;

import helper.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.ArticleRepository;

@RunWith(MockitoJUnitRunner.class)
public class CrawlerServiceTest {
    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    CrawlerService crawlerService;

    @Test
    public void test_getArticleLinksFromFileAsList_whenFileDoesntExist() {

    }

    // except empty list
    @Test
    public void test_getArticleLinksFromFileAsList_whenFileDataIsNotPresent() {

    }

    @Test
    public void test_getArticleLinksFromFileAsList_whenFileDataIsPresent() {

    }

    @Test
    public void test_articleLinkToUrL_when
}
