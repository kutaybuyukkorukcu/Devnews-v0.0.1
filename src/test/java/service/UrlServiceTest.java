package service;

import domain.Url;
import domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.UrlRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    UrlRepository urlRepository;

    @InjectMocks
    UrlService urlService;

    @Test
    public void test_addUrl_whenNoUrlsArePresent() {

        Url url = new Url();

        doThrow(new NullPointerException())
                .doNothing()
                .when(urlService).addUrl(url);

        verify(urlRepository).add(url);
        verifyNoMoreInteractions(urlService);
    }

    @Test
    public void test_addUrl_whenOneUrlIsPresent() {

        Url url = new Url("www.infoq.com/Whats-new-with-Java-11", true);

        urlService.addUrl(url);

        verify(urlRepository).add(url);
        verifyNoMoreInteractions(urlService);
    }

    @Test
    public void test_getArticleLinks() {

        Url url = new Url("www.infoq.com/Whats-new-with-Java-11", true);
        Url url1 = new Url("www.dzone.com/Comprehensive-guide-to-unit-testing", true);

        List<String> articleLinkList = new ArrayList();
        articleLinkList.add("www.infoq.com/Whats-new-with-Java-11");
        articleLinkList.add("www.dzone.com/Comprehensive-guide-to-unit-testing");

        when(urlRepository.findAllByIsNew()).thenReturn(asList(url, url1));

        List<String> expectedArticleLinkList = urlService.getArticleLinksAsList();

        verify(urlRepository).findAllByIsNew();

        assertThat(articleLinkList).isEqualTo(expectedArticleLinkList);
        verifyNoMoreInteractions();
    }
}
