package com.fitnessapp.management.security.token;

public interface TokenConfig {


    long getExpiryMs();


    long getExpirySeconds();


    String getPath();


    String getType();
}
