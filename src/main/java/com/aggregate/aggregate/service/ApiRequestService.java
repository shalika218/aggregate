package com.aggregate.aggregate.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ApiRequestService {

    private final WebClient webClient;

    public ApiRequestService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<ResponseEntity<Object>> makeApiRequest(String url, String token, Map<String, Object> data) {
        // Print the URL and token for the API request
        System.out.println("Making API request to: " + url);
        System.out.println("Using token: " + token);
        System.out.println("Request body data: " + data);

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(data)
                .retrieve()
                .toEntity(Object.class) // Retrieve the response as an entity
                .flatMap(response -> {
                    // Log the successful response here
                    System.out.println("Received response: " + response.getBody());
                    return Mono.just(response);
                })
                .doOnError(e -> {
                    // Log the error if something goes wrong
                    System.err.println("Error in API request: " + e.getMessage());
                });
    }
}
