package com.example.Articles.service;

import com.example.Articles.entity.Article;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleService {

    List<Article> searchArticlesByTitle(String title);
    List<Article> getAllArticles();
    Article createArticle(Article article);
    Article updateArticle(Long id, Article updatedArticle);
    void deleteArticle(Long id);
    List<Article> getArticlesByTag(Long tagId);
    List<Article> searchArticles(String query);
    List<Article> getAllArticlesSorted();
    Optional<Article> getArticleById(Long id);
    List<Article> getArticlesByUser(Long userId);



}
