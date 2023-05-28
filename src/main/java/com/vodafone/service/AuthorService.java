package com.vodafone.service;

import com.vodafone.model.Author;

import java.util.List;

public interface AuthorService {
    Author getAuthorById(Integer id);
    List<Author> getAuthors();


}
