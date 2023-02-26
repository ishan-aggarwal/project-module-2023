package com.ishan.blogapi.profiles.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProfileResponseDTO {
    String username;
    String email;
    String bio;
}
