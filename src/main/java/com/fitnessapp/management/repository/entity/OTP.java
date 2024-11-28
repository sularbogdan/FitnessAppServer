package com.fitnessapp.management.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OTP {
    String code;
    Long creationDate;
}
