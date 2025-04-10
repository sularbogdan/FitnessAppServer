package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.AvatarNotFoundException;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.AvatarRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.AvatarDTO;
import com.fitnessapp.management.repository.entity.Avatar;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.AvatarService;
import com.fitnessapp.management.config.MapperConfig;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;
    private final MapperConfig mapperConfig;
    private UserRepository userRepository;

    public AvatarServiceImpl(AvatarRepository avatarRepository, MapperConfig mapperConfig, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.mapperConfig = mapperConfig;
        this.userRepository=userRepository;
    }

    @Override
    public List<AvatarDTO> getAllAvatars() {
        return Arrays.asList(mapperConfig.mapToDto(avatarRepository.findAll(), AvatarDTO[].class));
    }

    public String convertToBase64(Avatar avatar) {
        if (avatar.getData() != null) {
            return Base64.getEncoder().encodeToString(avatar.getData());
        }
        return null;
    }
    public AvatarDTO getAvatarByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Avatar avatar = user.getAvatar();
        if (avatar == null) {
            throw new AvatarNotFoundException("Avatar not found for user: " + username);
        }

        AvatarDTO avatarDTO = new AvatarDTO();
        avatarDTO.setId(avatar.getId());
        avatarDTO.setFileName(avatar.getFileName());
        avatarDTO.setFileType(avatar.getFileType());
        avatarDTO.setBase64Image(convertToBase64(avatar));
        return avatarDTO;
    }

}
