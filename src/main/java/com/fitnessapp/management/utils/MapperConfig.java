package com.fitnessapp.management.utils;

import com.fitnessapp.management.repository.dto.UserResponseDTO;
import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import com.fitnessapp.management.repository.entity.User;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperConfig {

    private final ModelMapper modelMapper;

    public MapperConfig() {
        this.modelMapper = new ModelMapper();
    }
    public <D, T> D mapToDto(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> T mapToEntity(D dto, Class<T> outClass) {
        return modelMapper.map(dto, outClass);
    }

    public <D, T> List<D> mapToList(Collection<T> entityList, Class<D> outClass) {
        return entityList.stream()
                .map(entity -> mapToDto(entity, outClass))
                .collect(Collectors.toList());
    }

    public UserSecurityDTO mapToUserSecurityDTO(Object entity) {
        return modelMapper.map(entity, UserSecurityDTO.class);
    }

    @PostConstruct
    public void setupMappings() {
        modelMapper.typeMap(User.class, UserResponseDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getAvatar().getData(), UserResponseDTO::setAvatarData);
            mapper.map(src -> src.getAvatar().getFileName(), UserResponseDTO::setAvatarFileName);
            mapper.map(src -> src.getAvatar().getFileType(), UserResponseDTO::setAvatarFileType);
        });
    }



}
