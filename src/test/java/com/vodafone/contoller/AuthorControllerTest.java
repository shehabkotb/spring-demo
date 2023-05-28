package com.vodafone.contoller;

import com.vodafone.model.Author;
import com.vodafone.service.AuthorService;
import com.vodafone.util.TestUtil;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorService authorService;

    @Test
    void getAuthorsTest_returnAuthorsJson() throws Exception {
        Author author1 = new Author(1, "shehab");
        Author author2 = new Author(2, "mostafa");
        List<Author> mockData = Arrays.asList(author1, author2);
        when(authorService.getAuthors()).thenReturn(mockData);

        mockMvc.perform(get("/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name", equalTo(author1.getName())))
                .andExpect(jsonPath("$.[0].id", equalTo(author1.getId())));

    }

    @Test
    void getAuthorById_returnAuthor() throws Exception {
        Author author1 = new Author(1, "shehab");
        when(authorService.getAuthorById(1)).thenReturn(author1);

        mockMvc.perform(get("/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", equalTo(author1.getName())))
                .andExpect(jsonPath("id", equalTo(author1.getId())));

    }
}