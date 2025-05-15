package com.fitnessapp.management.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendResetPasswordHtmlEmail(String to, String password);

}
