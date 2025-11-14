package com.vaadin.example.ai.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;

@Service
public class SampleService {

    private final RestClient restClient = RestClient.create();

    @Tool(description = "Calculate factorial of a number")
    public BigInteger factorial(
        @ToolParam(description = "The number to calculate the factorial of") int n
    ) {
        System.out.println("Calculating factorial of " + n );
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    @Tool(description = "Fetch today's electricity prices from Liukuri.fi")
    public String fetchTodaysElectricityPricesJson() {
        System.out.println("Fetching today's electricity prices from Liukuri.fi");
        try {
            return restClient
                .get()
                .uri("https://liukuri.fi/api/todaysPrices.json")
                .retrieve()
                .body(String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch today's electricity prices", e);
        }
    }
}
