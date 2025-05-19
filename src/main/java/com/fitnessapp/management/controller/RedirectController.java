package com.fitnessapp.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/success")
    public String success() {
        return "Your payment was completed successfully!";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "The payment was canceled.";
    }
}

