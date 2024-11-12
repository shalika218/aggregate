// package com.aggregate.aggregate.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import java.nio.charset.StandardCharsets;
// import java.util.Base64;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1")
// public class TokenController {

// @PostMapping("/generate-token")
// public ResponseEntity<String> generateToken(@RequestBody Map<String, String>
// credentials) {
// // Extract client_id and client_secret from the request body
// String clientId = credentials.get("client_id");
// String clientSecret = credentials.get("client_secret");

// // Combine clientId and clientSecret with a colon
// String authString = clientId + ":" + clientSecret;

// // Encode the string in Base64
// String encodedAuth =
// Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

// // Create Bearer token format
// String token = "Bearer " + encodedAuth;

// // Return the token as the response
// return ResponseEntity.ok(token);
// }
// }
