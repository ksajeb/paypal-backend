package com.paypal.api_gateway.security;

import com.paypal.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    public static final List<String> PUBLIC_PATHS= List.of(
            "/auth/signup",
            "/auth/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path=exchange.getRequest().getPath().value();
        String normalizedPath=path.replaceAll("/+$","");
        if(PUBLIC_PATHS.contains(normalizedPath)){
            return chain.filter(exchange)
                    .doOnSubscribe(s-> System.out.println("Proceeding without check"))
                    .doOnSuccess(v-> System.out.println("Successfully passed."))
                    .doOnError(e-> System.out.println("Error occurred"));
        }

        String authHeader=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try{
            String token=authHeader.substring(7);
            Claims claims= jwtUtil.validateToken(token);
            exchange.getRequest().mutate()
                    .header("X-User-Email",claims.getSubject())
                    .header("X-User-Id",claims.get("userId",String.class))
                    .header("X-User-Role",claims.get("role",String.class))
                    .build();

            return chain.filter(exchange)
                    .doOnSubscribe(s-> System.out.println("Proceeding without check"))
                    .doOnSuccess(v-> System.out.println("Successfully passed."))
                    .doOnError(e-> System.out.println("Error occurred"));
        }catch (Exception ex){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
