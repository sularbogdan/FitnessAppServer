package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.*;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.UserRequestDTO;
import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.entity.enums.Role;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.UserService;
import com.fitnessapp.management.utils.MapperConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MapperConfig mapperConfig;


    public UserServiceImpl(UserRepository userRepository, MapperConfig mapperConfig) {
        this.userRepository = userRepository;
        this.mapperConfig = mapperConfig;
    }

    @Override
    public UserResponseDTO getUserByUsernameOrEmail(String username, String email){
        User user = userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new UsernameOrEmailNotFoundException("Username or email not found!"));
        return mapperConfig.mapToDto(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        return mapperConfig.mapToDto(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new EmailNotFoundException("Email not found!"));
        return mapperConfig.mapToDto(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return mapperConfig.mapToDto(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByRole(Role role){
        User user = userRepository.findUserByRole(role)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return mapperConfig.mapToDto(user, UserResponseDTO.class);

    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return mapperConfig.mapToList(users, UserResponseDTO.class);
    }

    @Override
    public Boolean isFirstLogin(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user.getFirstLogin();

    }
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        userRepository.delete(user);
    }
}



