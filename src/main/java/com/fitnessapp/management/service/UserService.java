package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.repository.entity.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponseDTO addUser(String username, String email);
    UserResponseDTO getUserByUsernameOrEmail(String username, String email);
    <T> T getUserByUsername(String username, Class<T> type);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByRole(Role role);
    List<UserResponseDTO> getAllUsers();
    void deleteUser(Long id);
}
