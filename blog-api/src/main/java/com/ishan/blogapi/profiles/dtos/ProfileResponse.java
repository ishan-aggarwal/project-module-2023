package com.ishan.blogapi.profiles.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProfileResponse {
    private List<ProfileResponseDTO> content;
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalElements;
    private Boolean last;
}
