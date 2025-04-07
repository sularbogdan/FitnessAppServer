package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.AvatarNotFoundException;
import com.fitnessapp.management.repository.AvatarRepository;
import com.fitnessapp.management.repository.dto.AvatarDTO;
import com.fitnessapp.management.repository.entity.Avatar;
import com.fitnessapp.management.service.AvatarService;
import com.fitnessapp.management.utils.MapperConfig;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;
    private final MapperConfig mapperConfig;

    public AvatarServiceImpl(AvatarRepository avatarRepository, MapperConfig mapperConfig) {
        this.avatarRepository = avatarRepository;
        this.mapperConfig = mapperConfig;
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

    public AvatarDTO getAvatarByUsername(String username) throws AvatarNotFoundException {
        Avatar avatar = avatarRepository.findByUser_Username(username);
        if (avatar == null) {
            throw new AvatarNotFoundException("Avatar not found for user: " + username);
        }
        AvatarDTO avatarDTO = new AvatarDTO();
        avatarDTO.setFileName(avatar.getFileName());
        avatarDTO.setFileType(avatar.getFileName());
        avatarDTO.setBase64Image(convertToBase64(avatar));
        avatarDTO.setId(avatar.getId());
        return avatarDTO;
    }
}
