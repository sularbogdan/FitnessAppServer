package com.fitnessapp.management.controller;

import com.fitnessapp.management.config.MapperConfig;
import com.fitnessapp.management.exception.AvatarNotFoundException;
import com.fitnessapp.management.repository.dto.AvatarDTO;

import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.repository.dto.UserUpdateDTO;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.repository.entity.enums.Role;
import com.fitnessapp.management.service.UserService;
import com.fitnessapp.management.service.implementation.AvatarServiceImpl;
import com.fitnessapp.management.service.implementation.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;
    private final AvatarServiceImpl avatarService;
    private final MapperConfig mapperConfig;

    public UserController(UserServiceImpl userService, AvatarServiceImpl avatarService, MapperConfig mapperConfig) {
        this.userService = userService;
        this.avatarService = avatarService;
        this.mapperConfig = mapperConfig;
    }

    @GetMapping("/usernameOrEmail")
    public UserResponseDTO getUserByUsernameOrEmail(@RequestParam String username, @RequestParam String email) {
        return userService.getUserByUsernameOrEmail(username, email);
    }

    @GetMapping("/username/{username}")
    public UserSecurityDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username, UserSecurityDTO.class);
    }

    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/role")
    public UserResponseDTO getUserByRole(@RequestParam Role role) {
        return userService.getUserByRole(role);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllClients() {
        List<User> clients = userService.getAllUsers();
        List<UserResponseDTO> dtoList = clients.stream()
                .map(user -> mapperConfig.mapToDto(user, UserResponseDTO.class))
                .toList();
        return ResponseEntity.ok(dtoList);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/get-avatar-by-username")
    public ResponseEntity<AvatarDTO> getAvatarByUsername(@RequestParam(name = "username") String username) {
        try {
            AvatarDTO avatarDTO = avatarService.getAvatarByUsername(username);
            return new ResponseEntity<>(avatarDTO, HttpStatus.OK);
        } catch (AvatarNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                               @RequestParam("username") String username) {
        try {
            userService.uploadAndSetAvatar(file, username);
            return ResponseEntity.ok("Avatar uploaded and saved!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/set-avatar")
    public ResponseEntity<String> setAvatar(@RequestBody Map<String, Object> payload) {
        Long avatarId = Long.valueOf(payload.get("avatarId").toString());
        String username = payload.get("username").toString();

        userService.setAvatarById(avatarId, username);
        return ResponseEntity.ok("Avatar set successfully");
    }

    @Transactional
    @PatchMapping("/update-profile")
    public UserResponseDTO updateUserByUsername(@RequestParam(value = "username") String username, @RequestBody UserUpdateDTO userUpdateDTO) {
        UserResponseDTO userResponseDTO = userService.updateUserByUsername(username, userUpdateDTO);
        return userResponseDTO;
    }




}
