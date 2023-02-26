package com.ishan.blogapi.users;

import com.ishan.blogapi.security.authtokens.AuthTokenService;
import com.ishan.blogapi.security.jwt.JWTService;
import com.ishan.blogapi.users.dto.CreateUserDTO;
import com.ishan.blogapi.users.dto.LoginUserDTO;
import com.ishan.blogapi.users.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthTokenService authTokenService;

    public UsersService(UsersRepository usersRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, JWTService jwtService, AuthTokenService authTokenService) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authTokenService = authTokenService;
    }

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        // TODO: Validate email
        Optional<UserEntity> userEntity = usersRepository.findByUsername(createUserDTO.getUsername());
        if (userEntity.isPresent()) {
            throw new UserAlreadyPresentException(createUserDTO.getUsername());
        }

        var newUserEntity = modelMapper.map(createUserDTO, UserEntity.class);
        newUserEntity.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        var savedUser = usersRepository.save(newUserEntity);
        var userResponseDTO = modelMapper.map(savedUser, UserResponseDTO.class);
        userResponseDTO.setToken(jwtService.createJWT(savedUser.getId()));

        return userResponseDTO;
    }

    public UserResponseDTO loginUser(LoginUserDTO loginUserDTO, AuthType authType) {
        Optional<UserEntity> userEntity = usersRepository.findByUsername(loginUserDTO.getUsername());
        if (userEntity.isEmpty()) {
            throw new UserNotFoundException(loginUserDTO.getUsername());
        }
        var passMatch = passwordEncoder.matches(loginUserDTO.getPassword(), userEntity.get().getPassword());
        if (!passMatch) {
            throw new IllegalArgumentException("Incorrect password");
        }
        var userResponseDTO = modelMapper.map(userEntity, UserResponseDTO.class);

        switch (authType) {
            case JWT:
                userResponseDTO.setToken(jwtService.createJWT(userEntity.get().getId()));
                break;
            case AUTH_TOKEN:
                userResponseDTO.setToken(authTokenService.createAuthToken(userEntity.get()).toString());
                break;
        }

        return userResponseDTO;
    }

    public static class UserAlreadyPresentException extends IllegalArgumentException {
        public UserAlreadyPresentException(Integer id) {
            super("User with id: " + id + " already present");
        }

        public UserAlreadyPresentException(String username) {
            super("User with username: " + username + " already present");
        }
    }

    public static class UserNotFoundException extends IllegalArgumentException {
        public UserNotFoundException(Integer id) {
            super("User with id: " + id + " not found");
        }

        public UserNotFoundException(String username) {
            super("User with username: " + username + " not found");
        }
    }

    public static class IncorrectPasswordException extends IllegalArgumentException {
        public IncorrectPasswordException() {
            super("Incorrect password");
        }
    }

    enum AuthType {
        JWT,
        AUTH_TOKEN
    }
}
