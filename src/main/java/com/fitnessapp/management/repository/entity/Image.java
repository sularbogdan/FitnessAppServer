package com.fitnessapp.management.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Lob
    @Column(name = "image_data")
    private byte[] image;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    public Image(String fileName, String fileType, byte[] image) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.image = image;
    }
}
