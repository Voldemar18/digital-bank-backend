package com.digitalbank.account.messaging.producer;

import com.digitalbank.account.model.dto.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage (TransactionEvent event) {
        String topic = "account-transactions";
        log.info("Отправка события перевода в Kafka. Отправитель: {}, Получатель: {}, Сумма: {} {}", event.getFromAccountId(), event.getToAccountId(), event.getAmount(), event.getCurrency());
        kafkaTemplate.send(topic, event);
    }
}