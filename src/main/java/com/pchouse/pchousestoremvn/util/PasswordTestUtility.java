package com.pchouse.pchousestoremvn.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * Utility class to test password encryption consistency across platforms
 * This helps identify if the authentication issue is related to character encoding
 */
public class PasswordTestUtility {
    
    /**
     * Test password encryption with explicit UTF-8 encoding
     * This should produce consistent results across all platforms
     */
    public static String testPasswordEncryption(String password) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Test password encryption with default charset (legacy method)
     * This may produce different results on different platforms
     */
    public static String testPasswordEncryptionLegacy(String password) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(password.getBytes());
    }
    
    /**
     * Run a comprehensive test to compare encryption methods
     */
    public static void runPasswordEncryptionTest() {
        String testPassword = "test123";
        
        System.out.println("=== Password Encryption Test ===");
        System.out.println("Test password: " + testPassword);
        System.out.println("System charset: " + System.getProperty("file.encoding"));
        System.out.println("Default charset: " + java.nio.charset.Charset.defaultCharset());
        
        String utf8Result = testPasswordEncryption(testPassword);
        String legacyResult = testPasswordEncryptionLegacy(testPassword);
        
        System.out.println("UTF-8 encrypted: " + utf8Result);
        System.out.println("Legacy encrypted: " + legacyResult);
        System.out.println("Results match: " + utf8Result.equals(legacyResult));
        System.out.println("================================");
    }
    
    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        runPasswordEncryptionTest();
    }
}
