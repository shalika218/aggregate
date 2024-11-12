package com.aggregate.aggregate.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Base64;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EncryptionService {

    // AES encryption key (16-byte key for AES-128 encryption)
    private static final String AES_KEY = "aesEncryptionKey";

    // Token expiration time (24 hours)
    private static final long EXPIRATION_TIME = 86400000;

    // Create ObjectMapper instance with JavaTimeModule registered
    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Handle Java 8 date and time types
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    // Retrieve SecretKey from the hardcoded AES key
    private SecretKey getSecretKey() {
        return new SecretKeySpec(AES_KEY.getBytes(), "AES");
    }

    // Encrypt the object data using AES
    public String encrypt(Object obj) throws Exception {
        // Convert the object to JSON using ObjectMapper
        String json = getObjectMapper().writeValueAsString(obj);

        // Encrypt using AES with the predefined key
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(json.getBytes());

        // Encode the encrypted bytes to Base64 to make it string-safe
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt the encrypted Base64 string back to an object of the specified type
    public <T> T decrypt(String encryptedData, Class<T> clazz) throws Exception {
        // Decode the Base64 encrypted string to get the encrypted bytes
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // Decrypt using AES with the predefined key
        SecretKey secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convert the decrypted bytes back to the original JSON and deserialize it into
        // the specified object type
        String decryptedJson = new String(decryptedBytes);
        return getObjectMapper().readValue(decryptedJson, clazz);
    }

    // Generate Bearer Token using client_id and client_secret
    public String generateBearerToken(String clientId, String clientSecret) {
        // Define claims (payload) to include in the token
        Map<String, Object> claims = new HashMap<>();
        claims.put("client_id", clientId);
        claims.put("client_secret", clientSecret);

        // Set token expiration date
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // Dynamically generate a secure secret key for HMAC-SHA256
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure 256-bit key

        // Create and sign the JWT
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256) // Use the secure key to sign the token
                .compact(); // Generate the token
    }
}
