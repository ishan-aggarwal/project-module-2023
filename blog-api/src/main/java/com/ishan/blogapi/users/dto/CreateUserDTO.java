package com.ishan.blogapi.users.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    String email;
    String username;
    String password;
    String bio;
    String image;
}
