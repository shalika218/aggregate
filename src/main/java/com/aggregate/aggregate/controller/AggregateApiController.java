// package com.aggregate.aggregate.controller;

// import java.util.Map;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.HttpStatus;

// @RestController
// @RequestMapping("/api/v1")
// public class AggregateApiController {

// @Autowired
// private RestTemplate restTemplate;

// // Inject API URLs from application.properties
// @Value("${api.insightlite.url}")
// private String insightLiteUrl;

// @Value("${api.searchlite.url}")
// private String searchLiteUrl;

// @Value("${api.tokenlite.url}")
// private String tokenLiteUrl;

// @Value("${api.accountlite.url}")
// private String accountLiteUrl;

// @Value("${api.tradelite.url}")
// private String tradeLiteUrl;

// @Value("${api.portfoliolite2.url}")
// private String portfolioLiteUrl;

// @Value("${api.strategylite.url}")
// private String strategyLiteUrl;

// @SuppressWarnings("unchecked")
// @PostMapping("/aggregate")
// public ResponseEntity<?> processInput(
// @RequestBody Map<String, Object> input,
// @RequestHeader("Authorization") String token) { // Token is passed in headers

// // Extract the type and data from the input JSON
// String type = (String) input.get("type");
// Map<String, Object> data = (Map<String, Object>) input.get("data");

// // Initialize headers and add the token
// HttpHeaders headers = new HttpHeaders();
// headers.set("Content-Type", "application/json");
// headers.set("Authorization", token); // Add token to headers

// // Define the external API URL based on the type
// String url;
// switch (type.toLowerCase()) {
// case "insightlite":
// url = insightLiteUrl;
// break;
// case "searchlite":
// url = searchLiteUrl;
// break;
// case "tokenlite":
// url = tokenLiteUrl;
// break;
// case "accountlite":
// url = accountLiteUrl;
// break;
// case "tradelite":
// url = tradeLiteUrl;
// break;
// case "portfoliolite2":
// url = portfolioLiteUrl;
// break;
// case "strategylite":
// url = strategyLiteUrl;
// break;
// default:
// return ResponseEntity.badRequest().body("Unsupported type: " + type);
// }

// // Create HttpEntity with data and headers (including token)
// HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);

// // Make the POST request to the external API
// try {
// ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST,
// entity, Object.class);
// return ResponseEntity.ok(response.getBody());
// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
// .body("Error calling external API: " + e.getMessage());
// }
// }
// }
