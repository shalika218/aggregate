package com.aggregate.aggregate.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.aggregate.aggregate.service.ApiRequestService;
import com.aggregate.aggregate.service.TokenService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1")
public class AggregateWebApiController {

    @Autowired
    private ApiRequestService apiRequestService;

    @Autowired
    private TokenService tokenService;

    @Value("${api.insightlite.url}")
    private String insightLiteUrl;

    @Value("${api.searchlite.url}")
    private String searchLiteUrl;

    @Value("${api.tokenlite.url}")
    private String tokenLiteUrl;

    @Value("${api.accountlite.url}")
    private String accountLiteUrl;

    @Value("${api.tradelite.url}")
    private String tradeLiteUrl;

    @Value("${api.portfoliolite2.url}")
    private String portfolioLiteUrl;

    @Value("${api.strategylite.url}")
    private String strategyLiteUrl;

    @SuppressWarnings("unchecked")
    @PostMapping("/aggregate")
    public Mono<ResponseEntity<Object>> processInput(
            @RequestBody Map<String, Object> input,
            @RequestHeader("Authorization") String loginToken) {

        // Extract the type and data from the input JSON
        String type = (String) input.get("type");
        Map<String, Object> data = (Map<String, Object>) input.get("data");

        // Define the external API URL based on the type
        String url = getUrlBasedOnType(type);

        if (url == null) {
            return Mono.just(ResponseEntity.badRequest().body("Unsupported type: " + type));
        }

        // **Always generate bearer token** for specific API types
        if (requiresBearerToken(type)) {
            return tokenService.getBearerToken()
                    .flatMap(bearerToken -> apiRequestService.makeApiRequest(url, bearerToken, data))
                    .onErrorResume(e -> {
                        System.err.println("Error calling external API: " + e.getMessage());
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error calling external API: " + e.getMessage()));
                    });
        } else {
            // Use the login token for APIs that don't need the bearer token
            return apiRequestService.makeApiRequest(url, loginToken, data)
                    .onErrorResume(e -> {
                        System.err.println("Error calling external API: " + e.getMessage());
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error calling external API: " + e.getMessage()));
                    });
        }
    }

    private String getUrlBasedOnType(String type) {
        switch (type.toLowerCase()) {
            case "insightlite":
                return insightLiteUrl;
            case "searchlite":
                return searchLiteUrl;
            case "tokenlite":
                return tokenLiteUrl;
            case "accountlite":
                return accountLiteUrl;
            case "tradelite":
                return tradeLiteUrl;
            case "portfoliolite2":
                return portfolioLiteUrl;
            case "strategylite":
                return strategyLiteUrl;
            default:
                return null;
        }
    }

    // Define which API types require the bearer token (always true for certain
    // types)
    private boolean requiresBearerToken(String type) {
        return type.equalsIgnoreCase("insightlite") || type.equalsIgnoreCase("searchlite") ||
                type.equalsIgnoreCase("tokenlite") || type.equalsIgnoreCase("accountlite") ||
                type.equalsIgnoreCase("tradelite") || type.equalsIgnoreCase("portfoliolite2") ||
                type.equalsIgnoreCase("strategylite");
    }
}
