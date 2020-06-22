package service;

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
        verifyNoMoreInteractions();
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
        verifyNoMoreInteractions();
    }

    // getRecommendations() - likeService bos donsun
//    @Test
//    public void test_getRecommendations_when

    // getRecommendations() - likeService dolu donsun, recommendationService.getRecommendation'i mocklamaya calis
    // jsonObject full bos donsun

    // getRecommendations() - likeService dolu donsun, recommendationService.getRecommendation'i mocklamaya calis
    // jsonObject dolu donsun ve verify(recommendationIntoRecommendationList)
}