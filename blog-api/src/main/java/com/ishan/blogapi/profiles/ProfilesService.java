package com.ishan.blogapi.profiles;

import com.ishan.blogapi.profiles.dtos.ProfileResponse;
import com.ishan.blogapi.profiles.dtos.ProfileResponseDTO;
import com.ishan.blogapi.users.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfilesService {

    private final ProfilesRepository profilesRepository;

    private final ModelMapper modelMapper;

    public ProfilesService(ProfilesRepository profilesRepository, ModelMapper modelMapper) {
        this.profilesRepository = profilesRepository;
        this.modelMapper = modelMapper;
    }

    public ProfileResponse getAllProfiles(Integer page, Integer limit, String sortBy, String sortDirection) {
        // create Sort instance
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(page, limit, sort);

        // get page object
        Page<UserEntity> userEntities = profilesRepository.findAll(pageable);

        // get content for page object
        List<UserEntity> usersList = userEntities.getContent();

        // convert content to response
        List<ProfileResponseDTO> userProfiles = usersList.stream().map(eachUser -> modelMapper.map(eachUser, ProfileResponseDTO.class)).collect(Collectors.toList());

        return getProfileResponse(userEntities, userProfiles);
    }

    private static ProfileResponse getProfileResponse(Page<UserEntity> userEntities, List<ProfileResponseDTO> userProfiles) {
        ProfileResponse response = new ProfileResponse();
        response.setContent(userProfiles);
        response.setPageNo(userEntities.getNumber());
        response.setPageSize(userEntities.getSize());
        response.setTotalElements(userEntities.getTotalElements());
        response.setTotalPages(userEntities.getTotalPages());
        response.setLast(userEntities.isLast());
        return response;
    }

    public ProfileResponseDTO getProfileByUsername(String username) {
        var userEntity = profilesRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        ProfileResponseDTO profileResponseDTO = modelMapper.map(userEntity, ProfileResponseDTO.class);
        return profileResponseDTO;
    }

    public static class UserNotFoundException extends IllegalArgumentException {
        public UserNotFoundException(Integer id) {
            super("User with id: " + id + " not found");
        }

        public UserNotFoundException(String username) {
            super("User with username: " + username + " not found");
        }
    }
}
