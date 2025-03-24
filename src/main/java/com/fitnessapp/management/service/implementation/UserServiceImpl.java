package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.*;
import com.fitnessapp.management.repository.UserRepository;
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
    public UserResponseDTO addUser(String username, String email) {
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(Role.CLIENT);
        return mapperConfig.mapToDto(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserByUsernameOrEmail(String username, String email){
        User user = userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new UsernameOrEmailNotFoundException("Username or email not found!"));
        return mapperConfig.mapToDto(user, UserResponseDTO.class);
    }

    @Override
    public <T> T getUserByUsername(String username, Class<T> type){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        return mapperConfig.mapToDto(user, type);
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
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        userRepository.delete(user);
    }
}



