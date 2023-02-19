package com.ishan.blogapi.comments.dtos;

import lombok.*;

@Data
@Builder
public class CreateCommentDTO {

    @NonNull
    private String title;
    @NonNull
    private String body;
}
