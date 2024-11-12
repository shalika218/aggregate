package com.aggregate.aggregate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TokenService {
    private final WebClient webClient;
    private final String generateBearerTokenUrl;

    public TokenService(WebClient.Builder webClientBuilder,
            @Value("${api.generate-bearer-token.url}") String generateBearerTokenUrl) {
        this.webClient = webClientBuilder.build();
        this.generateBearerTokenUrl = generateBearerTokenUrl;
    }

    public Mono<String> getBearerToken() {
        TokenGeneratedInput input = new TokenGeneratedInput("crypto88@gmail.com");

        return webClient.post()
                .uri(generateBearerTokenUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(input)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    System.out.println("Bearer token response: " + response);
                    // Assuming response contains a JSON string, parse the token manually
                    if (response.contains("access_token")) {
                        String token = response.substring(response.indexOf(":") + 2, response.length() - 2);
                        return token;
                    }
                    return response;
                })
                .onErrorResume(e -> {
                    System.err.println("Error generating bearer token: " + e.getMessage());
                    return Mono.empty();
                });
    }
}
