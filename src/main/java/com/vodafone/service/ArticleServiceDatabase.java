package com.vodafone.service;

import com.vodafone.contoller.ArticlesControlerV3;
import com.vodafone.contoller.ArticlesController;
import com.vodafone.contoller.AuthorController;
import com.vodafone.contoller.AuthorControllerV2;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Links;
import com.vodafone.repository.ArticleRepository;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Qualifier("ArticleServiceDatabase")
public class ArticleServiceDatabase implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;


    @Override
    public List<Article> getAllArticles() {
        List<Article> result = articleRepository.findAll().stream().map(this::addLinks).collect(Collectors.toList());
        return result;
    }

    @Override
    public Article getArticleById(Integer id) {
        Optional<Article> result = articleRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException(String.format("The Article with id '%s' was not found", id));
        }
        return addLinks(result.get());
    }

    @Override
    public Article addArticle(Article article) {
        Article result = articleRepository.save(article);
        return addLinks(result);
    }

    @Override
    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }

    @Override
    public Article updateArticle(Integer id, Article article) {
        article.setId(id);
        Article result = articleRepository.save(article);
        return result;
    }

    private Article addLinks(Article article){
        List<Links> links = new ArrayList<>();
        Links self = new Links();

        Link selfLink = linkTo(methodOn(ArticlesControlerV3.class)
                .getArticle(article.getId())).withRel("self");

        self.setRel("self");
        self.setHref(selfLink.getHref());

        Links authorLink = new Links();
        Link authLink = linkTo(methodOn(AuthorControllerV2.class)
                .getAuthorById(article.getAuthorId())).withRel("author");
        authorLink.setRel("author");
        authorLink.setHref(authLink.getHref());

        links.add(self);
        links.add(authorLink);
        article.setLinks(links);
        return article;
    }

    @Override
    public List<Article> getArticlesByAuthorName(String authorName) {
       throw new NotYetImplementedException("todo: needs query in repository");
    }
}
