package com.ishan.blogapi.users;

import com.ishan.blogapi.security.authtokens.AuthTokenRepository;
import com.ishan.blogapi.security.authtokens.AuthTokenService;
import com.ishan.blogapi.security.jwt.JWTService;
import com.ishan.blogapi.users.dto.CreateUserDTO;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UsersServiceTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuthTokenRepository authTokenRepository;
    private UsersService usersService;

    private UsersService getUsersService() {
        if (usersService == null) {
            var modelMapper = new ModelMapper();
            var passwordEncoder = new BCryptPasswordEncoder();
            var jwtService = new JWTService();
            var authTokenService = new AuthTokenService(authTokenRepository);
            usersService = new UsersService(usersRepository, modelMapper, passwordEncoder, jwtService, authTokenService);
        }
        return usersService;
    }


    @Test
    public void testCreateUser() {
        var newUserDTO = new CreateUserDTO();
        newUserDTO.setEmail("ishan.aggarwal@gmail.com");
        newUserDTO.setPassword("Password123");
        newUserDTO.setUsername("ishan-aggarwal");
        var savedUser = getUsersService().createUser(newUserDTO);
        assertNotNull(savedUser);
    }
}