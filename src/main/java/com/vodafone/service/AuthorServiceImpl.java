package com.vodafone.service;

import com.vodafone.model.Article;
import com.vodafone.model.Author;
import org.hibernate.bytecode.spi.NotInstrumentedException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class AuthorServiceImpl implements AuthorService {
    List<Author> authors = new ArrayList<>();
    Integer id = 0;

    public AuthorServiceImpl() {
        Author author = new Author();
        author.setId(1);
        author.setName("ahmed");
        authors.add(author);
    }

    @Override
    public Author getAuthorById(Integer id) {
        for (Author author : authors) {
            if (id.equals(author.getId())) {
                return author;
            }
        }
        return null;
    }

    @Override
    public List<Author> getAuthors() {
        return authors;
    }
}
