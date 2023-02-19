package com.ishan.blogapi.users;

import com.ishan.blogapi.users.dto.CreateUserDTO;
import com.ishan.blogapi.users.dto.LoginUserDTO;
import com.ishan.blogapi.users.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public UsersService(
            UsersRepository usersRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        // TODO: Validate email

        // Check if username already exists
        UserEntity user = usersRepository.findByUsername(createUserDTO.getUsername());
        if (user != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        var newUserEntity = modelMapper.map(createUserDTO, UserEntity.class);
        // Encrypt password
        newUserEntity.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        var savedUser = usersRepository.save(newUserEntity);
        var userResponseDTO = modelMapper.map(savedUser, UserResponseDTO.class);

        return userResponseDTO;
    }

    public UserResponseDTO loginUser(LoginUserDTO loginUserDTO) {
        var userEntity = usersRepository.findByUsername(loginUserDTO.getUsername());
        if (userEntity == null) {
            throw new UserNotFoundException(loginUserDTO.getUsername());
        }
        // Encrypt password and compare with the one in the database
        if (!userEntity.getPassword().equals(passwordEncoder.encode(loginUserDTO.getPassword()))) {
            throw new IllegalArgumentException("Incorrect password");
        }
        var userResponseDTO = modelMapper.map(userEntity, UserResponseDTO.class);
        return userResponseDTO;
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


}
