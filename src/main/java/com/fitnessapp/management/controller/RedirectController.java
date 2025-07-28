package com.fitnessapp.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    @GetMapping("/success")
    public String success() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta http-equiv='refresh' content='5;URL=http://localhost:5173/dashboard-client' />" +
                "<title>Payment Successful</title>" +
                "</head>" +
                "<body>" +
                "<h2>Your payment was completed successfully!</h2>" +
                "<p>You will be redirected to your dashboard in 5 seconds...</p>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return  "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta http-equiv='refresh' content='5;URL=http://localhost:5173/dashboard-client' />" +
                "<title>Payment Canceled</title>" +
                "</head>" +
                "<body>" +
                "<h2>The payment was canceled.</h2>" +
                "<p>You will be redirected to your dashboard in 5 seconds...</p>" +
                "</body>" +
                "</html>";
    }
}

