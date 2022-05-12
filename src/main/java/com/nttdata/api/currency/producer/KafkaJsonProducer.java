package com.nttdata.api.currency.producer;

import com.nttdata.api.currency.document.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaJsonProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaJsonProducer.class);

    private final KafkaTemplate<String, List<Currency>> kafkaTemplate;

    public KafkaJsonProducer(@Qualifier("kafkaJsonTemplate") KafkaTemplate<String, List<Currency>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCurrency(List<Currency> c) {
        LOGGER.info("Enviando moneda", c);
        this.kafkaTemplate.send("topic-currency", c);
    }

}
