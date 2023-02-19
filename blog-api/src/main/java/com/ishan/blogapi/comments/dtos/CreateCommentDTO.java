package com.ishan.blogapi.comments.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateCommentDTO {

    @NonNull
    private String title;
    @NonNull
    private String body;
}
