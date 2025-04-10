package com.fitnessapp.management.service;


import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserUpdateDTO;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.repository.entity.enums.Role;
import com.fitnessapp.management.security.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    UserResponseDTO addUser(RegisterRequest registerRequest);
    UserResponseDTO getUserByUsernameOrEmail(String username, String email);
    <T> T getUserByUsername(String username, Class<T> type);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByRole(Role role);
    List<User> getAllUsers();
    void deleteUser(Long id);
    void uploadAndSetAvatar(MultipartFile file, String username) throws IOException;
    void setAvatarById(Long avatarId, String username);
    UserDetails loadUserByUsername(String username);
    UserResponseDTO updateUserByUsername(String username, UserUpdateDTO userUpdateDTO);



}
