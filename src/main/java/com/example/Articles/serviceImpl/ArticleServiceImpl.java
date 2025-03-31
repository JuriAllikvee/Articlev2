package com.example.Articles.serviceImpl;

import com.example.Articles.entity.Article;
import com.example.Articles.repository.ArticleRepository;
import com.example.Articles.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }
    @Override
    public List<Article> searchArticles(String query) {
        return articleRepository.findByTitleContainingIgnoreCase(query);
    }


    @Override
    public Article updateArticle(Long id, Article updatedArticle) {
        return articleRepository.findById(id)
                .map(article -> {
                    article.setTitle(updatedArticle.getTitle());
                    article.setDescription(updatedArticle.getDescription());
                    article.setSlug(updatedArticle.getSlug());
                    article.setContent(updatedArticle.getContent());
                    article.setTags(updatedArticle.getTags());
                    return articleRepository.save(article);
                })
                .orElseThrow(() -> new IllegalArgumentException("Статья не найдена"));
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }



    @Override
    public List<Article> getArticlesByTag(Long tagId) {
        return articleRepository.findByTags_Id(tagId);
    }

    @Override
    public List<Article> searchArticlesByTitle(String title) {
        return articleRepository.findByTitleContainingIgnoreCase(title);
    }
    @Override
    public List<Article> getAllArticlesSorted() {
        return articleRepository.findAllByOrderByCreatedAtDesc();
    }
    @Override
    public List<Article> getArticlesByUser(Long userId) {
        return articleRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }



}
