package com.example.Articles.repository;

import com.example.Articles.entity.Article;
import com.example.Articles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleContainingIgnoreCase(String title);
    List<Article> findByTags_Id(Long tagId);
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByUser(User user);
    List<Article> findByUserIdOrderByCreatedAtDesc(Long userId);

}

