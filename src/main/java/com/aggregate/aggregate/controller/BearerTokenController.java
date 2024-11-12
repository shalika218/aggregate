// package com.aggregate.aggregate.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import com.aggregate.aggregate.service.EncryptionService;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1")
// public class BearerTokenController {

// private final EncryptionService encryptionService;

// public BearerTokenController(EncryptionService encryptionService) {
// this.encryptionService = encryptionService;
// }

// @PostMapping("/generate-bearer-token")
// public ResponseEntity<String> generateBearerToken(@RequestBody Map<String,
// String> credentials) {
// // Extract client_id and client_secret from the request body
// String clientId = credentials.get("client_id");
// String clientSecret = credentials.get("client_secret");

// // Generate the Bearer token using the EncryptionService
// String token = encryptionService.generateBearerToken(clientId, clientSecret);

// // Return the Bearer token as a response
// return ResponseEntity.ok("Bearer " + token);
// }
// }
