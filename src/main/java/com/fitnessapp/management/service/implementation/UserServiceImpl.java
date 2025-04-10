package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.*;
import com.fitnessapp.management.repository.AvatarRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserUpdateDTO;
import com.fitnessapp.management.repository.entity.Avatar;
import com.fitnessapp.management.repository.entity.enums.Role;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.security.request.RegisterRequest;
import com.fitnessapp.management.security.response.UserDetailsImpl;
import com.fitnessapp.management.service.UserService;
import com.fitnessapp.management.config.MapperConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperConfig mapperConfig;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, MapperConfig mapperConfig, AvatarRepository avatarRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapperConfig = mapperConfig;
        this.avatarRepository = avatarRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO addUser(RegisterRequest request) {
        if (userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setActive(true);
        user.setRole(Role.CLIENT);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        Avatar defaultAvatar = avatarRepository.findFirstByFileName("avatar1.png")
                .orElseThrow(() -> new RuntimeException("Default avatar not found"));

        Avatar clonedAvatar = new Avatar();
        clonedAvatar.setFileName(UUID.randomUUID().toString());
        clonedAvatar.setFileType(defaultAvatar.getFileType());
        clonedAvatar.setData(defaultAvatar.getData());

        user.setAvatar(clonedAvatar);

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
        User user = userRepository.findByEmail(email)
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
    public List<User> getAllUsers() {
        return userRepository.findAllByRole(Role.CLIENT);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        userRepository.delete(user);
    }

    @Override
    public void uploadAndSetAvatar(MultipartFile file, String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        Avatar avatar = user.getAvatar();
        if (avatar == null) {
            avatar = new Avatar();
        }
        avatar.setFileName(file.getOriginalFilename());
        avatar.setFileType(file.getContentType());
        avatar.setData(file.getBytes());

        user.setAvatar(avatar);
        userRepository.save(user);
    }

    @Override
    public void setAvatarById(Long avatarId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new AvatarNotFoundException("Avatar not found!"));

        user.setAvatar(avatar);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return UserDetailsImpl.build(user);
    }
    @Override
    public UserResponseDTO updateUserByUsername(String username, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            return new UserNotFoundException("User not found");
        });

        if (userUpdateDTO.getEmail() != null) {
            if (userRepository.findByEmail(userUpdateDTO.getEmail()).isEmpty()) {
                user.setEmail(userUpdateDTO.getEmail());
            } else {
                throw new EmailAlreadyExistException("Email already exists!");
            }
        }

        if (userUpdateDTO.getAvatarId() != null) {
            Avatar avatar = avatarRepository
                    .findById(userUpdateDTO.getAvatarId())
                    .orElseThrow(() -> {
                        return new AvatarNotFoundException("Avatar not found!");
                    });
            user.setAvatar(mapperConfig.mapToDto(avatar, Avatar.class));
        }

        if (userUpdateDTO.getUpdatedImage()) {
            user.setImage(userUpdateDTO.getImage());
        }

        if (userUpdateDTO.getFirstName() != null) {
            user.setFirstName(userUpdateDTO.getFirstName());
        }
        if (userUpdateDTO.getLastName() != null) {
            user.setLastName(userUpdateDTO.getLastName());
        }

        userRepository.save(user);
        return mapperConfig.mapToDto(user, UserResponseDTO.class);
    }

}



