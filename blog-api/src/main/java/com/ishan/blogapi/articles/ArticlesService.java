package com.ishan.blogapi.articles;

import com.ishan.blogapi.articles.dtos.ArticleResponseDTO;
import com.ishan.blogapi.articles.dtos.CreateArticleDTO;
import com.ishan.blogapi.articles.dtos.UpdateArticleDTO;
import com.ishan.blogapi.comments.CommentEntity;
import com.ishan.blogapi.comments.CommentsRepository;
import com.ishan.blogapi.comments.dtos.CommentResponseDTO;
import com.ishan.blogapi.comments.dtos.CreateCommentDTO;
import com.ishan.blogapi.users.UserEntity;
import com.ishan.blogapi.users.UsersRepository;
import com.ishan.blogapi.users.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final UsersRepository usersRepository;

    private final CommentsRepository commentsRepository;

    private final ModelMapper modelMapper;

    public ArticlesService(ArticlesRepository articlesRepository, UsersRepository usersRepository, CommentsRepository commentsRepository, ModelMapper modelMapper) {
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
        this.commentsRepository = commentsRepository;
        this.modelMapper = modelMapper;
    }

    public List<ArticleEntity> getAllArticles() {
        return articlesRepository.findAll();
    }

    public ArticleResponseDTO getArticleBySlug(String slug) {
        ArticleEntity articleEntity = articlesRepository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));
        ArticleResponseDTO articleResponseDTO = modelMapper.map(articleEntity, ArticleResponseDTO.class);
        return articleResponseDTO;
    }

    public ArticleResponseDTO createArticle(Integer authorId, CreateArticleDTO createArticleDTO) {
        var author = usersRepository.findById(authorId).orElseThrow(() -> new UsersService.UserNotFoundException(authorId));
        ArticleEntity articleEntity = articlesRepository.save(ArticleEntity.builder()
                .title(createArticleDTO.getTitle())
                .slug(getSlug(createArticleDTO.getTitle()))
                .body(createArticleDTO.getBody())
                .subtitle(createArticleDTO.getSubtitle())
                .author(author)
                .tagList(createArticleDTO.getTagList())
                .build()
        );
        ArticleResponseDTO articleResponseDTO = modelMapper.map(articleEntity, ArticleResponseDTO.class);
        articleResponseDTO.setUsername(author.getUsername());
        articleResponseDTO.setEmail(author.getEmail());

        return articleResponseDTO;
    }

    private String getSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-") + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public ArticleResponseDTO updateArticle(Integer authorId, String slug, UpdateArticleDTO updateArticleDTO) {

        ArticleEntity article = articlesRepository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));

        if (article.getAuthor().getId() != authorId) {
            throw new UnsupportedOperationException("Author with id : " + authorId + " is not authorized to update article : " + slug);
        }

        if (updateArticleDTO.getBody() != null) {
            article.setBody(updateArticleDTO.getBody());
        }

        if (updateArticleDTO.getSubtitle() != null) {
            article.setSubtitle(updateArticleDTO.getSubtitle());
        }

        if (!CollectionUtils.isEmpty(updateArticleDTO.getTagList())) {
            article.setTagList(updateArticleDTO.getTagList());
        }

        ArticleEntity articleEntity = articlesRepository.save(article);
        ArticleResponseDTO articleResponseDTO = modelMapper.map(articleEntity, ArticleResponseDTO.class);
        return articleResponseDTO;
    }

    public List<ArticleResponseDTO> getAllArticles(Integer page, Integer limit, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<ArticleEntity> paginatedArticles = articlesRepository.findAll(pageable);
        List<ArticleEntity> articleEntityList = paginatedArticles.getContent();

        List<ArticleResponseDTO> articleResponseDTOList = new ArrayList();
        for (ArticleEntity articleEntity : articleEntityList) {
            ArticleResponseDTO articleResponseDTO = modelMapper.map(articleEntity, ArticleResponseDTO.class);
            UserEntity author = articleEntity.getAuthor();
            articleResponseDTO.setUsername(author.getUsername());
            articleResponseDTO.setEmail(author.getEmail());
            articleResponseDTOList.add(articleResponseDTO);
        }
        return articleResponseDTOList;
    }

    public List<ArticleResponseDTO> getAllArticlesByAuthor(String author, Integer page, Integer limit, String sortBy, String sortDirection) {
        UserEntity userEntity = usersRepository.findByUsername(author).orElseThrow(() -> new UsersService.UserNotFoundException(author));
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(page, limit, sort);

        List<ArticleEntity> articleEntities = articlesRepository.findByAuthorId(userEntity.getId(), pageable);
        List<ArticleResponseDTO> articleResponseDTOList = new ArrayList();
        for (ArticleEntity articleEntity : articleEntities) {
            ArticleResponseDTO articleResponseDTO = modelMapper.map(articleEntity, ArticleResponseDTO.class);
            articleResponseDTO.setUsername(userEntity.getUsername());
            articleResponseDTO.setEmail(userEntity.getEmail());
            articleResponseDTOList.add(articleResponseDTO);
        }
        return articleResponseDTOList;
    }

    public List<ArticleResponseDTO> getAllArticlesByTag(String tag, Integer page, Integer limit, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(page, limit, sort);

        List<ArticleEntity> articleEntities = articlesRepository.findByTagListContaining(tag, pageable);
        List<ArticleResponseDTO> articleResponseDTOList = new ArrayList();
        for (ArticleEntity articleEntity : articleEntities) {
            ArticleResponseDTO articleResponseDTO = modelMapper.map(articleEntity, ArticleResponseDTO.class);
            articleResponseDTO.setUsername(articleEntity.getAuthor().getUsername());
            articleResponseDTO.setEmail(articleEntity.getAuthor().getEmail());
            articleResponseDTOList.add(articleResponseDTO);
        }
        return articleResponseDTOList;
    }

    public CommentResponseDTO addCommentOnArticle(Integer userId, String slug, CreateCommentDTO createCommentDTO) {
        ArticleEntity article = articlesRepository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));
        UserEntity user = usersRepository.findById(userId).orElseThrow(() -> new UsersService.UserNotFoundException(userId));

        CommentEntity savedCommentEntity = commentsRepository.save(CommentEntity.builder()
                .title(createCommentDTO.getTitle())
                .body(createCommentDTO.getBody())
                .article(article)
                .author(user)
                .build()
        );

        CommentResponseDTO commentResponseDTO = modelMapper.map(savedCommentEntity, CommentResponseDTO.class);
        return commentResponseDTO;
    }

    public List<CommentResponseDTO> getPaginatedCommentsForArticleBySlug(String slug, Integer page, Integer limit, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        ArticleEntity article = articlesRepository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));
        // create Pageable instance
        Pageable pageable = PageRequest.of(page, limit, sort);

        // get comments
        List<CommentEntity> commentEntities = commentsRepository.findByArticleId(article.getId(), pageable);

        // map to response DTO
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList();
        for (CommentEntity commentEntity : commentEntities) {
            CommentResponseDTO commentResponseDTO = modelMapper.map(commentEntity, CommentResponseDTO.class);
            commentResponseDTO.setUsername(commentEntity.getAuthor().getUsername());
            commentResponseDTO.setArticleSlug(commentEntity.getArticle().getSlug());
            commentResponseDTOList.add(commentResponseDTO);
        }
        return commentResponseDTOList;
    }

    public CommentResponseDTO deleteCommentOnArticleByCommentId(Integer userId, String slug, Integer commentId) {

        ArticleEntity article = articlesRepository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));
        CommentEntity commentEntity = commentsRepository.findByIdAndArticleId(commentId, article.getId()).orElseThrow(() -> new CommentNotFoundException(commentId, article.getId()));
        UserEntity userEntity = usersRepository.findById(userId).orElseThrow(() -> new UsersService.UserNotFoundException(userId));

        if (commentEntity.getAuthor().getId() != userId) {
            throw new UnsupportedOperationException("Author with id : " + userId + " is not authorized to delete comment : " + commentEntity.getTitle());
        }
        commentsRepository.delete(commentEntity);
        CommentResponseDTO commentResponseDTO = modelMapper.map(commentEntity, CommentResponseDTO.class);
        commentResponseDTO.setUsername(userEntity.getUsername());
        return commentResponseDTO;
    }


    public static class ArticleNotFoundException extends IllegalArgumentException {
        public ArticleNotFoundException(String slug) {
            super("Article " + slug + " not found");
        }

        public ArticleNotFoundException(Integer id) {
            super("Article with id: " + id + " not found");
        }
    }

    public static class CommentNotFoundException extends IllegalArgumentException {
        public CommentNotFoundException(Integer id) {
            super("Comment with id: " + id + " not found");
        }

        public CommentNotFoundException(Integer commentId, Integer articleId) {
            super("Comment with id: " + commentId + " not found for article with id: " + articleId);
        }
    }
}