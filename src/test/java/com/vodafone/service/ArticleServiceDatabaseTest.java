package com.vodafone.service;

import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.repository.ArticleRepository;
import com.vodafone.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ArticleServiceDatabaseTest {

    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setUp() throws Exception {
        articleService = new ArticleServiceDatabase(articleRepository);
    }

    @Test
    void getAllArticlesTest_returnArticleJson() {
        Article Article1 = Article.builder()
                .id(1)
                .name("article1")
                .author(Author.builder().id(1).name("shehab1").build())
                .build();
        List<Article> mockData = Arrays.asList(Article1);
        when(articleRepository.findAll()).thenReturn(mockData);

        List<Article> result = articleService.getAllArticles();

        assertThat(result).hasSize(1).contains(Article1);
    }

    @Test
    void getArticleByIdTest_givenArticleExists_returnArticle() {
        Article article1 = Article.builder()
                .id(1)
                .name("article1")
                .author(new Author(1, "shehab"))
                .build();
        when(articleRepository.findById(1)).thenReturn(Optional.of(article1));

        Article result = articleService.getArticleById(1);

        assertThat(result).isEqualTo(article1);
    }

    @Test
    void getArticleByIdTest_givenArticleDoesNotExists_throwException() {
        when(articleRepository.findById(anyInt())).thenThrow(new NotFoundException("Article Not Found"));


        assertThatThrownBy(() -> articleService.getArticleById(2))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Article Not Found");
    }

    @Test
    void deleteArticleTest_verifyRepositoryIsCalled() {
        int idToDelete = 10;

        doNothing().when(articleRepository).deleteById(idToDelete);

        articleService.deleteArticle(10);

        verify(articleRepository).deleteById(idToDelete);
    }
}