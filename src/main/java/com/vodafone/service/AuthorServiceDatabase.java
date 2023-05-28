package com.vodafone.service;

import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Author;
import com.vodafone.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("AuthorServiceDatabase")
public class AuthorServiceDatabase implements AuthorService {

    AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceDatabase(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getAuthorById(Integer id) {
        Optional<Author> result = authorRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Author Not Found");
        }
        return result.get();
    }

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }
}
