package com.ishan.blogapi.articles.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class CreateArticleDTO {
    @NonNull
    private String title;
    private String subtitle;
    @NonNull
    private String body;
    private List<String> tagList;
}
