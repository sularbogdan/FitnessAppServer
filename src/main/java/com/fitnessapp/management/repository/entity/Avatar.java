package com.fitnessapp.management.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private Long id;

    @Column(name = "file_name", unique = true)
    private String fileName;


    @Column(name = "file_type")
    private String fileType;

    @Column(name = "data", length = 10000)
    private byte[] data;



    public Avatar(String fileName, byte[] data, String fileType) {
        this.fileName = fileName;
        this.data = data;
        this.fileType = fileType;
    }
}
