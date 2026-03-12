package com.java.gateway.component;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> WHITELIST = Arrays.asList(
            "/auth/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        String iotAuthHeader = exchange.getRequest().getHeaders().getFirst("AotAuth");
        String iotRefAuthHeader = exchange.getRequest().getHeaders().getFirst("AotRefAuth");
        String path = exchange.getRequest().getPath().value();

        if (WHITELIST.stream().anyMatch(p -> path.matches(p.replace("**", ".*")))) {
            return chain.filter(exchange);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        String username;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // If username is valid and IoT headers exist, add them to the body
        if (username != null && iotAuthHeader != null && iotRefAuthHeader != null) {
            // Mutate the request body
            return exchange.getRequest().getBody()
                    .collectList()
                    .flatMap(dataBuffers -> {
                        String body = dataBuffers.stream()
                                .map(buffer -> {
                                    byte[] bytes = new byte[buffer.readableByteCount()];
                                    buffer.read(bytes);
                                    DataBufferUtils.release(buffer);
                                    return new String(bytes, StandardCharsets.UTF_8);
                                })
                                .collect(Collectors.joining());

                        // Add IoT tokens and username to JSON body
                        ObjectNode jsonNode = null;
                        try {
                            jsonNode = new ObjectMapper().readValue(body, ObjectNode.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        jsonNode.put("username", username);
                        jsonNode.put("iotAccessToken", iotAuthHeader);
                        jsonNode.put("iotRefreshToken", iotRefAuthHeader);

                        byte[] newBodyBytes = jsonNode.toString().getBytes(StandardCharsets.UTF_8);
                        DataBuffer newBuffer = exchange.getResponse().bufferFactory().wrap(newBodyBytes);

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(newBodyBytes.length))
                                .build();

                        return chain.filter(exchange.mutate()
                                .request(mutatedRequest)
                                .build());
                    });
        }

        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return -1;
    }

}
