package com.fitnessapp.management.user;

import com.fitnessapp.management.config.MapperConfig;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.AvatarRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserUpdateDTO;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.EmailService;
import com.fitnessapp.management.service.UserService;
import com.fitnessapp.management.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperConfig mapperConfig;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User mockUser;
    private UserResponseDTO mockDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setUsername("test");
        mockUser.setEmail("test@test.com");

        mockDTO = new UserResponseDTO();
        mockDTO.setUsername("test");
        mockDTO.setEmail("test@test.com");
    }

    @Test
    void testGetUserByUsernameOrEmail() {

        when(userRepository.findByUsernameOrEmail("test", "test@test.com")).thenReturn(Optional.of(mockUser));
        when(mapperConfig.mapToDto(mockUser,UserResponseDTO.class)).thenReturn(mockDTO);

        UserResponseDTO result = userService.getUserByUsernameOrEmail("test", "test@test.com");

        assertEquals("test", result.getUsername());
        verify(userRepository).findByUsernameOrEmail("test", "test@test.com");
    }

    @Test
    void testGetUserById() {

        when(userRepository.findById(99l)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        userService.deleteUser(1l);
        verify(userRepository).delete(mockUser);
    }

    @Test
    void testGetUserbyEmail() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        when(mapperConfig.mapToDto(mockUser,UserResponseDTO.class)).thenReturn(mockDTO);
        UserResponseDTO result = userService.getUserByEmail("test@test.com");
        assertEquals("test@test.com", result.getEmail());
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void testGetUserByUsername(){
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(mockUser));
        when(mapperConfig.mapToDto(mockUser, UserResponseDTO.class)).thenReturn(mockDTO);
        UserResponseDTO responseDTO = userService.getUserByUsername("test", UserResponseDTO.class);
        assertEquals("test", responseDTO.getUsername());
        verify(userRepository).findByUsername("test");
    }

    @Test
    void testResetPassword() {
        userService = new UserServiceImpl(userRepository, mapperConfig, null, passwordEncoder, emailService);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        userService.resetPassword("test@test.com");
        verify(userRepository).save(mockUser);
        verify(emailService).sendResetPasswordHtmlEmail(eq("test@test.com"), anyString());
    }

    @Test
    void testUpdateUserByUsername() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setFirstName("NewFirst");
        updateDTO.setLastName("NewLast");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(mapperConfig.mapToDto(mockUser, UserResponseDTO.class)).thenReturn(mockDTO);
        UserResponseDTO result = userService.updateUserByUsername("test", updateDTO);
        assertEquals("test", result.getUsername());
        verify(userRepository).save(mockUser);
    }


}
