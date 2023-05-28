package com.vodafone.contoller;

import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticlesController.class)
class ArticlesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArticleService articleService;

    @Test
    void getArticlesTest_returnArticlesJson() throws Exception {
        Article Article1 = Article.builder()
                .id(1)
                .name("article1")
                .author(
                        Author.builder()
                                .id(1)
                                .name("shehab1")
                                .build()
                )
                .build();
        List<Article> mockData = Arrays.asList(Article1);
        when(articleService.getAllArticles()).thenReturn(mockData);

        mockMvc.perform(get("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].name", equalTo(Article1.getName())))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].authorId").isNumber())
                .andExpect(jsonPath("$.[0].author").value("shehab1"));
    }

    @Test
    void getArticleByIdTest_givenArticleExists_returnArticle() throws Exception {
        Article Article1 = Article.builder()
                .id(1)
                .name("article1")
                .author(
                        Author.builder()
                                .id(1)
                                .name("shehab1")
                                .build()
                )
                .build();
        when(articleService.getArticleById(1)).thenReturn(Article1);

        mockMvc.perform(get("/v1/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(Article1.getName())))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.authorId").isNumber())
                .andExpect(jsonPath("$.author").value("shehab1"));

    }

    @Test
    void getArticleByIdTest_givenArticleDoesNotExists_ThrowException() throws Exception {
        when(articleService.getArticleById(anyInt())).thenThrow(new NotFoundException("The Article with id 1 was not found"));

        mockMvc.perform(get("/v1/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void addArticleTest_returnCreatedArticleJson() throws Exception {
        Article mockResult = Article.builder()
                .id(1)
                .name("article1")
                .author(Author.builder().id(1).name("shehab1").build())
                .build();

        String inputArticle = "{\"name\":\"amazingArticle1\",\"author\":{\"name\":\"shehab1\"}}";

        when(articleService.addArticle(any())).thenReturn(mockResult);

        mockMvc.perform(post("/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputArticle)
                        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("article1")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.authorId").isNumber())
                .andExpect(jsonPath("$.author").value("shehab1"));
    }


    @Test
    void deleteArticleTest_returnNoContent() throws Exception {
        int idToDelete = 10;

        doNothing().when(articleService).deleteArticle(idToDelete);

        mockMvc.perform(delete(String.format("/v1/articles/%s", idToDelete)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(articleService).deleteArticle(idToDelete);


    }
}