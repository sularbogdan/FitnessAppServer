package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.dto.AvatarDTO;
import com.fitnessapp.management.repository.entity.Avatar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AvatarService {

    List<AvatarDTO> getAllAvatars();

}
