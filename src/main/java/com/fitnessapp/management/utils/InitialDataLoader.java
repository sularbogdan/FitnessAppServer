package com.fitnessapp.management.utils;

import com.fitnessapp.management.repository.AvatarRepository;
import com.fitnessapp.management.repository.entity.Avatar;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private final AvatarRepository avatarRepository;

    public InitialDataLoader(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (avatarRepository.count() == 0) {
            List<String> fileNames = List.of("avatar1.png" ,"avatar2.png", "avatar3.png","avatar4.png");
            List<Avatar> avatarsToSave = new ArrayList<>();

            for (String fileName : fileNames) {
                try {
                    ClassPathResource imageResource = new ClassPathResource("avatars/" + fileName);
                    byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());
                    String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
                    Avatar avatar = new Avatar(fileName, imageBytes, fileType);
                    avatarsToSave.add(avatar);
                } catch (IOException e) {
                    System.err.println("Failed to load avatar: " + fileName);
                    e.printStackTrace();
                }
            }

            avatarRepository.saveAll(avatarsToSave);
            System.out.println("Avatare default incarcate.");
        } else {
            System.out.println("Avatare deja existente in bd.");
        }
    }
}
