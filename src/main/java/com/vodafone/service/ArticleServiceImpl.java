package com.vodafone.service;

import com.vodafone.contoller.ArticlesController;
import com.vodafone.contoller.AuthorController;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Links;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Primary
public class ArticleServiceImpl implements ArticleService {
    List<Article> articles = new ArrayList<>();
    Integer id = 0;

    @Override
    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        for (Article article : articles) {
            Article articleWithLinks = addLinks(article);
            articleList.add(articleWithLinks);
        }
        return articleList;
    }

    @Override
    public Article getArticleById(Integer id) {
        for (Article article : articles) {
            if (id.equals(article.getId())) {
                Article articleWithLinks = addLinks(article);
                return articleWithLinks;
            }
        }
       throw new NotFoundException(String.format("The Article with id '%s' was not found", id));
    }

    @Override
    public List<Article> getArticlesByAuthorName(String authorName) {
        List<Article> articleList = new ArrayList<>();
        for (Article article : articles) {
            if (authorName.equals(article.getAuthor())) {
                Article articleWithLinks = addLinks(article);
                articleList.add(articleWithLinks);
            }
        }
        return articleList;
    }

    @Override
    public Article addArticle(Article article) {
        article.setId(++id);
        articles.add(article);
        Article articleWithLinks = addLinks(article);
        return articleWithLinks;
    }

    @Override
    public void deleteArticle(Integer id) {
        Article articleById = getArticleById(id);
        articles.remove(articleById);
    }

    @Override
    public Article updateArticle(Integer id, Article article) {
        deleteArticle(id);
        article.setId(id);
        articles.add(article);
        Article articleWithLinks = addLinks(article);
        return articleWithLinks;
    }

    private Article addLinks(Article article){
        List<Links> links = new ArrayList<>();
        Links self = new Links();

        Link selfLink = linkTo(methodOn(ArticlesController.class)
                .getArticle(article.getId())).withRel("self");

        self.setRel("self");
        self.setHref(selfLink.getHref());

        Links authorLink = new Links();
        Link authLink = linkTo(methodOn(AuthorController.class)
                .getAuthorById(article.getAuthorId())).withRel("author");
        authorLink.setRel("author");
        authorLink.setHref(authLink.getHref());

        links.add(self);
        links.add(authorLink);
        article.setLinks(links);
        return article;
    }
}
