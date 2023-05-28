package com.vodafone.service;

import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Author;
import com.vodafone.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceDatabaseTest {

    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() throws Exception {
        authorService = new AuthorServiceDatabase(authorRepository);
    }

    @Test
    void getAuthorsTest_returnAuthorList() {

        Author author1 = new Author(1, "shehab");
        Author author2 = new Author(2, "mostafa");
        List<Author> mockData = Arrays.asList(author1, author2);
        when(authorRepository.findAll()).thenReturn(mockData);

        List<Author> result = authorService.getAuthors();

        assertThat(result).hasSize(2).contains(author1, author2);

    }

    @Test
    void getAuthorByIdTest_givenAuthorExists_returnAuthor() {
        Author author1 = new Author(1, "shehab");
        when(authorRepository.findById(1)).thenReturn(Optional.of(author1));

        Author result = authorService.getAuthorById(1);

        assertThat(result).isEqualTo(author1);
    }
    @Test
    void getAuthorByIdTest_givenAuthorDoesNotExist_ThrowNotFoundException() {
        when(authorRepository.findById(anyInt())).thenThrow(new NotFoundException("Author Not Found"));


        assertThatThrownBy(() -> authorService.getAuthorById(2))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Author Not Found");
    }
}