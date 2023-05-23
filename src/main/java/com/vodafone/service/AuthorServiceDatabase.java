package com.vodafone.service;

import com.vodafone.model.Author;
import com.vodafone.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("AuthorServiceDatabase")
public class AuthorServiceDatabase implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public Author getAuthorById(Integer id) {
        Optional<Author> result = authorRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get();
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }
}
