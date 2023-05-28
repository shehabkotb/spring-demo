package com.vodafone.contoller;

import com.vodafone.model.Article;
import com.vodafone.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class ArticlesController {

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/articles", produces = {"application/json"})
    public ResponseEntity<List<Article>> getArticles(@RequestParam(name = "author", required = false) String author) {
        List<Article> articles = new ArrayList<>();
        if (author != null) {
            articles = articleService.getArticlesByAuthorName(author);
        } else {
            articles = articleService.getAllArticles();
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping(value = "/articles/{id}", produces = {"application/json"})
    public ResponseEntity<Article> getArticle(@PathVariable(name = "id") Integer id) {
        Article article = articleService.getArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping(value = "/articles", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Article> addArticle(@Valid @RequestBody Article article) {
        article = articleService.addArticle(article);
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }
    @PutMapping(value = "/articles/{id}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Article> updateArticle(@PathVariable(name = "id") Integer id, @Valid @RequestBody(required = true) Article article) {
        article = articleService.updateArticle(id, article);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }
    @DeleteMapping(value = "/articles/{id}", produces = {"application/json"})
    public ResponseEntity<Article> deleteArticle(@PathVariable(name = "id") Integer id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
