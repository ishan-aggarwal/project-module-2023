package com.ishan.blogapi.articles;

import com.ishan.blogapi.articles.dtos.ArticleResponseDTO;
import com.ishan.blogapi.articles.dtos.CreateArticleDTO;
import com.ishan.blogapi.articles.dtos.UpdateArticleDTO;
import com.ishan.blogapi.comments.dtos.CommentResponseDTO;
import com.ishan.blogapi.comments.dtos.CreateCommentDTO;
import com.ishan.blogapi.commons.Constants;
import com.ishan.blogapi.profiles.dtos.ProfileResponse;
import com.ishan.blogapi.users.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

    private ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }


    @GetMapping("")
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles(
            @NonNull @RequestParam("page") String page,
            @NonNull @RequestParam("limit") String limit,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = Constants.DEFAULT_SORT_DIRECTION) String sortDirection) {
        return ResponseEntity.ok(articlesService.getAllArticles(Integer.valueOf(page), Integer.valueOf(limit), sortBy, sortDirection));
    }

    @RequestMapping(value = "", params = "author", method = RequestMethod.GET)
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticlesByAuthor(
            @NonNull @RequestParam("author") String author,
            @NonNull @RequestParam("page") String page,
            @NonNull @RequestParam("limit") String limit,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = Constants.DEFAULT_SORT_DIRECTION) String sortDirection) {
        return ResponseEntity.ok(articlesService.getAllArticlesByAuthor(author, Integer.valueOf(page), Integer.valueOf(limit), sortBy, sortDirection));
    }

    @RequestMapping(value = "", params = "tag", method = RequestMethod.GET)
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticlesByTag(
            @NonNull @RequestParam("tag") String tag,
            @NonNull @RequestParam("page") String page,
            @NonNull @RequestParam("limit") String limit,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = Constants.DEFAULT_SORT_DIRECTION) String sortDirection) {
        return ResponseEntity.ok(articlesService.getAllArticlesByTag(tag, Integer.valueOf(page), Integer.valueOf(limit), sortBy, sortDirection));
    }

    @GetMapping(value = "/{article-slug}")
    public ResponseEntity<ArticleResponseDTO> getArticleBySlug(
            @PathVariable("article-slug") String slug) {
        return ResponseEntity.ok(articlesService.getArticleBySlug(slug));
    }

    @PostMapping("")
    public ResponseEntity<ArticleResponseDTO> createArticle(@AuthenticationPrincipal Integer userId, @RequestBody CreateArticleDTO createArticleDTO) {
        var savedArticle = articlesService.createArticle(userId, createArticleDTO);
        return ResponseEntity
                .created(URI.create("/articles/" + savedArticle.getSlug()))
                .body(savedArticle);
    }

    @PatchMapping("/{article-slug}")
    public ResponseEntity<ArticleResponseDTO> updateArticleBySlug(
            @AuthenticationPrincipal Integer userId,
            @PathVariable("article-slug") String slug,
            @RequestBody UpdateArticleDTO updateArticleDTO) {
        var updatedArticle = articlesService.updateArticle(userId, slug, updateArticleDTO);
        return ResponseEntity.ok(updatedArticle);
    }

    @PostMapping("/{article-slug}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentOnArticle(
            @AuthenticationPrincipal Integer userId,
            @PathVariable("article-slug") String slug,
            @RequestBody CreateCommentDTO createCommentDTO) {
        CommentResponseDTO commentResponseDTO = articlesService.addCommentOnArticle(userId, slug, createCommentDTO);
        return ResponseEntity
                .created(URI.create("/articles/" + slug + "/comments/" + commentResponseDTO.getId()))
                .body(commentResponseDTO);
    }

    @RequestMapping(value = "/{article-slug}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<CommentResponseDTO>> getPaginatedCommentsForArticleBySlug(
            @PathVariable("article-slug") String slug,
            @NonNull @RequestParam("page") String page,
            @NonNull @RequestParam("limit") String limit,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = Constants.DEFAULT_SORT_DIRECTION) String sortDirection) {
        return ResponseEntity.ok(
                articlesService.getPaginatedCommentsForArticleBySlug(
                        slug,
                        Integer.valueOf(page),
                        Integer.valueOf(limit),
                        sortBy,
                        sortDirection));
    }

    @RequestMapping(value = "/{article-slug}/comments/{comment-id}", method = RequestMethod.DELETE)
    public ResponseEntity<CommentResponseDTO> deleteCommentOnArticleByCommentId(
            @AuthenticationPrincipal Integer userId,
            @PathVariable("article-slug") String slug,
            @PathVariable("comment-id") Integer commentId) {
        return ResponseEntity.ok(
                articlesService.deleteCommentOnArticleByCommentId(
                        userId,
                        slug,
                        commentId));
    }


    @GetMapping("/dummy")
    public String getArticles() {
        return "Articles";
    }

    @GetMapping("/dummy/private")
    public String getPrivateArticles(@AuthenticationPrincipal Integer userId) {
        return "Private Articles fetched for = " + userId;
    }

    @ExceptionHandler(ArticlesService.ArticleNotFoundException.class)
    public ResponseEntity<String> handleArticleNotFoundException(ArticlesService.ArticleNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ArticlesService.CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(ArticlesService.CommentNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<String> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}