package com.microservices.swotlyzer.api.gateway.config;

import com.microservices.swotlyzer.common.config.models.BaseUser;
import com.microservices.swotlyzer.common.config.utils.WebClientUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;
    public static final List<String> openApiEndpoints =
            List.of("/api/v1/auth-users/login", "/api/v1/auth-users/refresh","/api/v1/auth-users/validate-token",
                    "/api/v1/auth-users/users");

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.writeWith(Flux.just(new DefaultDataBufferFactory().wrap("Unauthorized".getBytes())));
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            if (!isSecured.test(request)) return chain.filter(exchange);
            var cookies = request.getCookies();
            if (!cookies.containsKey("accessToken"))
                return this.onError(exchange, "accessToken missing", HttpStatus.UNAUTHORIZED);
            ;
            var tokenCookie = cookies.get("accessToken").get(0);
            var tokenValue = tokenCookie.getValue();
            if (tokenValue.trim().length() == 0)
                return this.onError(exchange, "api-key missing", HttpStatus.UNAUTHORIZED);
            return webClientBuilder.build().get()
                    .uri("http://auth-user-service/api/v1/auth-users/validate-token?token=" +
                            URLEncoder.encode(tokenValue, StandardCharsets.UTF_8)).exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            return clientResponse.bodyToMono(String.class);
                        }
                        return clientResponse.bodyToMono(BaseUser.class);
                    }).flatMap(userOrBoolean -> {
                        //gato para validar se a resposta do microserviço de usuário foi um erro
                        if (userOrBoolean.getClass() != BaseUser.class)
                            return this.onError(exchange, "api-key missing", HttpStatus.UNAUTHORIZED);
                        BaseUser user = (BaseUser) userOrBoolean;
                        System.out.println(userOrBoolean);
                        System.out.println("bosta");
                        exchange.getRequest().mutate()
                                //adicionando os headers de usuário para a requisição
                                .header(WebClientUtils.X_AUTH_USER_LOGIN, user.getEmail())
                                .header(WebClientUtils.X_AUTH_USER_ID, String.valueOf(user.getId()));
                        return chain.filter(exchange);
                    });


        };
    }

    public static class Config {

    }
}
