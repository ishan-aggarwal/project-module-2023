package com.ishan.blogapi.profiles;

import com.ishan.blogapi.commons.Constants;
import com.ishan.blogapi.profiles.dtos.ProfileResponse;
import com.ishan.blogapi.profiles.dtos.ProfileResponseDTO;
import com.ishan.blogapi.users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfilesController {

    private final ProfilesService profilesService;

    public ProfilesController(ProfilesService profilesService) {
        this.profilesService = profilesService;
    }

    @GetMapping("")
    public ResponseEntity<ProfileResponse> getAllProfiles(
            @NonNull @RequestParam(value = "page") Integer page,
            @NonNull @RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "sortBy", defaultValue = "username") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = Constants.DEFAULT_SORT_DIRECTION) String sortDirection) {
        return ResponseEntity.ok(profilesService.getAllProfiles(page, limit, sortBy, sortDirection));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponseDTO> getProfileByUsername(@NonNull @PathVariable(value = "username") String username) {
        return ResponseEntity.ok(profilesService.getProfileByUsername(username));
    }

    // TODO: move to commons package
    @ExceptionHandler(UsersService.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsersService.UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


