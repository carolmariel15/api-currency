package com.nttdata.api.currency.router;

import com.nttdata.api.currency.handler.CurrencyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CurrencyRouter {

    @Bean
    public RouterFunction<ServerResponse> currencyRouterFunc(CurrencyHandler currencyHandler) {
        return RouterFunctions.route(GET("/currency").and(accept(MediaType.TEXT_EVENT_STREAM)), currencyHandler::getAllCurrency)
                .andRoute(GET("/currency/{id}").and(accept(MediaType.TEXT_EVENT_STREAM)), currencyHandler::getCurrency)
                .andRoute(POST("/currency").and(accept(MediaType.TEXT_EVENT_STREAM)), currencyHandler::create)
                .andRoute(PUT("/currency").and(accept(MediaType.TEXT_EVENT_STREAM)), currencyHandler::edit)
                .andRoute(DELETE("/currency/{id}").and(accept(MediaType.TEXT_EVENT_STREAM)), currencyHandler::delete);
    }

}
