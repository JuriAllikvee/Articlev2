package com.example.Articles.controllers;

import com.example.Articles.entity.Article;
import com.example.Articles.entity.User;
import com.example.Articles.service.ArticleService;
import com.example.Articles.service.TagService;
import com.example.Articles.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService, TagService tagService, UserService userService) {
        this.articleService = articleService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllArticles(Model model, Authentication authentication) {
        List<Article> articles = articleService.getAllArticlesSorted();
        model.addAttribute("articles", articles);

        String currentUsername = authentication != null ? authentication.getName() : null;
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("isAdmin", isAdmin);

        return "articles/list";
    }



    @GetMapping("/{id}")
    public String getArticleById(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Статья не найдена"));
        model.addAttribute("article", article);
        return "articles/details";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("tags", tagService.getAllTags());
        return "articles/add";
    }

    @PostMapping
    public String createArticle(@ModelAttribute Article article, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User currentUser = userService.findByUsername(username).orElseThrow();
        article.setUser(currentUser);
        articleService.createArticle(article);
        return "redirect:/articles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Article article = articleService.getArticleById(id).orElseThrow();
        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !article.getUser().getUsername().equals(username)) {
            return "redirect:/articles?error=not-authorized";
        }

        model.addAttribute("article", article);
        model.addAttribute("tags", tagService.getAllTags());
        return "articles/edit";
    }


    @PostMapping("/update/{id}")
    public String updateArticle(@PathVariable Long id, @ModelAttribute Article updatedArticle, Authentication authentication) {
        Article article = articleService.getArticleById(id).orElseThrow();
        String username = authentication.getName();

        if (!article.getUser().getUsername().equals(username)) {
            return "redirect:/articles?error=not-authorized";
        }

        article.setTitle(updatedArticle.getTitle());
        article.setDescription(updatedArticle.getDescription());
        article.setContent(updatedArticle.getContent());
        article.setSlug(updatedArticle.getSlug());
        article.setTags(updatedArticle.getTags());

        articleService.updateArticle(article.getId(), article);
        return "redirect:/articles";
    }


    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id, Authentication authentication) {
        Article article = articleService.getArticleById(id).orElseThrow();
        String username = authentication.getName();

        if (!article.getUser().getUsername().equals(username)) {
            return "redirect:/articles?error=not-authorized";
        }

        articleService.deleteArticle(id);
        return "redirect:/articles";
    }
    @GetMapping("/articles")
    public String listArticles(Model model, Authentication authentication) {
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);

        String currentUsername = authentication != null ? authentication.getName() : null;
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("isAdmin", isAdmin);

        return "articles/list";
    }
    @GetMapping("/tag/{tagId}")
    public String getArticlesByTag(@PathVariable Long tagId, Model model, Authentication authentication) {
        List<Article> articles = articleService.getArticlesByTag(tagId);
        model.addAttribute("articles", articles);

        String currentUsername = authentication != null ? authentication.getName() : null;
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("isAdmin", isAdmin);

        return "articles/list";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, Authentication authentication) {
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/articles";
        }

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/search")
    public String searchArticles(@RequestParam("query") String query, Model model, Authentication authentication) {
        List<Article> articles = articleService.searchArticlesByTitle(query);
        model.addAttribute("articles", articles);

        String currentUsername = authentication != null ? authentication.getName() : null;
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("isAdmin", isAdmin);

        return "articles/list";
    }
    @GetMapping("/user/{userId}")
    public String getArticlesByUser(@PathVariable Long userId, Model model, Authentication authentication) {
        List<Article> articles = articleService.getArticlesByUser(userId);
        model.addAttribute("articles", articles);

        String currentUsername = authentication != null ? authentication.getName() : null;
        boolean isAdmin = authentication != null && authentication.getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("currentUsername", currentUsername);
        model.addAttribute("isAdmin", isAdmin);

        return "articles/list";
    }





}

