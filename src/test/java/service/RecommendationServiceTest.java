package service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import domain.Article;
import domain.Like;
import domain.Recommendation;
import exception.ResourceNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.ArticleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationServiceTest {

    @Mock
    LikeService likeService;

    @Mock
    ArticleService articleService;

    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    RecommendationService recommendationService;

    @Test
    public void test_getTopRecommendationsFromList_whenSeveralRecommendationsArePresent() {
        Recommendation recommendation = new Recommendation(1, 0.35);
        Recommendation recommendation1 = new Recommendation(2,0.50);
        Recommendation recommendation2 = new Recommendation(3,0.85);

        List<Recommendation> recommendationList = new ArrayList();
        recommendationList.add(recommendation1);
        recommendationList.add(recommendation2);
        recommendationList.add(recommendation);

        List<Recommendation> expectedRecommendationList = recommendationService.getTopRecommendationsFromList(recommendationList);

        assertThat(expectedRecommendationList.get(0).getArticleId()).isEqualTo(1);
        assertThat(expectedRecommendationList.get(0).getSimilarityScore()).isEqualTo(0.35);

        assertThat(expectedRecommendationList.get(1).getArticleId()).isEqualTo(2);
        assertThat(expectedRecommendationList.get(1).getArticleId()).isEqualTo(0.50);

        assertThat(expectedRecommendationList.get(2).getArticleId()).isEqualTo(3);
        assertThat(expectedRecommendationList.get(2).getArticleId()).isEqualTo(0.85);
    }

    // recommendationListToArticleList - recommendations dolu, recommendArticles bos liste ata
    // findByArticleId() null donecek ve ResourceNoutFoundException() beklenecek
    @Test
    public void test_recommendationListToArticleList_whenArticleIsNotPresent() {
        Recommendation recommendation = new Recommendation(1, 0.35);

        List<Recommendation> recommendationList = new ArrayList();
        recommendationList.add(recommendation);

        int articleID = recommendationList.get(0).getArticleId();

        Article article = new Article();
        List<Article> recommendedArticles = new ArrayList<>();

        when(articleRepository.findByArticleId(articleID)).thenReturn(article);

        doThrow(new ResourceNotFoundException())
                .doNothing()
                .when(recommendationService).recommendationListToArticleList(recommendationList, recommendedArticles);

        verify(articleRepository).findByArticleId(articleID);
        verifyNoMoreInteractions(recommendationService);
    }

    // recommendationListToArticleList - recommendation dolu, recommendedArticles bos liste ata
    // findByArticleId() dolu donecek ve recommendedArticles uzerinde assert islemi gerceklestirilecek
    @Test
    public void test_recommendationListToArticleList_whenArticleIsPresent() {
        Recommendation recommendation = new Recommendation(1, 0.35);

        List<Recommendation> recommendationList = new ArrayList();
        recommendationList.add(recommendation);

        int articleID = recommendationList.get(0).getArticleId();

        Article article = new Article(1,"Whats new with Java 11", "Development", "Author",
                "Development|Java", "www.infoq.com/Whats-new-with-Java-11", true);

        List<Article> recommendedArticles = new ArrayList<>();

        when(articleRepository.findByArticleId(articleID)).thenReturn(article);

        recommendationService.recommendationListToArticleList(recommendationList, recommendedArticles);

        assertThat(recommendedArticles).hasSize(1);
        assertThat(recommendedArticles.get(0)).isEqualTo(article);

        verify(articleRepository).findByArticleId(articleID);
        verifyNoMoreInteractions(recommendationService);
    }

    // getRecommendations() - likeService bos donsun
    @Test
    public void test_getRecommendations_whenGetNewLikesIsNotPresent() {
        List<Like> likeList = new ArrayList();

        when(likeService.getNewLikes()).thenReturn(likeList);

        doThrow(new ResourceNotFoundException())
                .doNothing()
                .when(recommendationService).getRecommendations();

        verify(likeService).getNewLikes();
        verifyNoMoreInteractions(recommendationService);
    }

    // getRecommendations() - likeService dolu donsun, recommendationService.getRecommendation'i mocklamaya calis
    // jsonObject full bos donsun
    @Test
    public void test_getRecommendations_whenGetRecommendationIsNotPresent() {
        Like like = new Like("What's new with Java 11", "Development", true);
        Like like1 = new Like("Comprehensive guide to unit testing", "Development", true);

        List<Like> likeList = new ArrayList();
        likeList.add(like);
        likeList.add(like1);

        when(likeService.getNewLikes()).thenReturn(likeList);

        JsonObject jsonObject = new JsonObject();
        // Not sure about mocking a service that was initiated using @InjectMocks
        when(recommendationService.getRecommendation(likeList.get(0).getTitle())).thenReturn(jsonObject);

        doThrow(new ResourceNotFoundException())
                .doNothing()
                .when(recommendationService).getRecommendations();

        verify(likeService).getNewLikes();
        verify(recommendationService).getRecommendation(likeList.get(0).getTitle());
        verifyNoMoreInteractions(recommendationService);
    }

    // getRecommendations() - likeService dolu donsun, recommendationService.getRecommendation'i mocklamaya calis
    // jsonObject dolu donsun ve verify(recommendationIntoRecommendationList)
    @Test
    public void test_getRecommendations_whenEverythingIsPresent() {
        Like like = new Like("What's new with Java 11", "Development", true);
        Like like1 = new Like("Comprehensive guide to unit testing", "Development", true);

        List<Like> likeList = new ArrayList();
        likeList.add(like);
        likeList.add(like1);

        when(likeService.getNewLikes()).thenReturn(likeList);

        List<Recommendation> recommendationList = Arrays.asList(
                new Recommendation(395, 0.42356506617672646),
                new Recommendation(250, 0.2579225416660869),
                new Recommendation(468, 0.2302017341361332),
                new Recommendation(248, 0.2230720491097254),
                new Recommendation(490, 0.19212489538396202));

        List<Recommendation> initializedStaticList = new ArrayList<>();

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(recommendationList);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("list", jsonElement);

        // Not sure about mocking a service that was initiated using @InjectMocks
        when(recommendationService.getRecommendation(likeList.get(0).getTitle())).thenReturn(jsonObject);

        verify(likeService).getNewLikes();
        verify(recommendationService).getRecommendation(likeList.get(0).getTitle());
        // Not sure about this but will try it.
        verify(recommendationService).recommendationIntoRecommendationList(jsonObject, initializedStaticList);
        verifyNoMoreInteractions(recommendationService);
    }
}