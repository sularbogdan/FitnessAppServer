package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.AvatarDTO;
import com.fitnessapp.management.service.AvatarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;



    public AvatarController(AvatarService avatarService){
        this.avatarService = avatarService;
    }

    @GetMapping("/all")
    public ResponseEntity<List <AvatarDTO>> getAllAvatars(){
        List<AvatarDTO> avatars = avatarService.getAllAvatars();
        return new ResponseEntity<>(avatars, HttpStatus.OK);
    }


}
