package com.example.weather_project.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordUtils {
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static boolean verifyPassword(String defaultPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(defaultPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}