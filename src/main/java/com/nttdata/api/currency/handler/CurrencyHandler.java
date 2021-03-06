package com.nttdata.api.currency.handler;

import com.nttdata.api.currency.document.Currency;
import com.nttdata.api.currency.producer.KafkaJsonProducer;
import com.nttdata.api.currency.repository.CurrecyRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CurrencyHandler {

    private final CurrecyRepository currencyRepository;

    private final KafkaJsonProducer kafkaJsonProducer;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllCurrency(ServerRequest serverRequest) {
        return  ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(currencyRepository.findAll().log(), Currency.class);
    }

    public Mono<ServerResponse> getCurrency(ServerRequest serverRequest) {
        var id = Integer.parseInt(serverRequest.pathVariable("id"));
        var currency = currencyRepository.findById(id);

        return currency.flatMap(i -> {
            kafkaJsonProducer.sendCurrency(i);
            return ServerResponse.ok()
                            .contentType(MediaType.TEXT_EVENT_STREAM)
                            .body(currency, Currency.class);
        }).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        var currencyMono = serverRequest.bodyToMono(Currency.class);
        return  currencyMono.flatMap(c -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(currencyRepository.save(c), Currency.class));
    }

    public Mono<ServerResponse> edit(ServerRequest serverRequest) {
        var itemEdit = serverRequest.bodyToMono(Currency.class);
        return itemEdit.filter(i -> i.getId()==4).flatMap(v -> {
            return currencyRepository.findById(v.getId()).flatMap(c -> {
                c.setDescription(v.getDescription());
                c.setBuyingRate(v.getBuyingRate());
                c.setSellingRate(v.getSellingRate());
                return ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(currencyRepository.save(c), Currency.class);
            }).switchIfEmpty(notFound);
        }).switchIfEmpty(ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .bodyValue("No se puede editar la tasa de compra y/o venta."));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        var id = Integer.parseInt(serverRequest.pathVariable("id"));
        return currencyRepository.findById(id)
                .flatMap(c -> ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(currencyRepository.delete(c), Void.class))
                .switchIfEmpty(notFound);
    }

}
