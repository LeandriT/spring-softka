package ec.banco.pichincha.account_service.producer.customer;

import ec.banco.pichincha.account_service.producer.KafkaDispatcher;
import ec.banco.pichincha.account_service.producer.customer.dto.TransactionCustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static ec.banco.pichincha.account_service.producer.topics.Topics.TOPIC_TRANSACTION_EVENT;

@Component
@RequiredArgsConstructor
public class EventAccountCustomerPublisher {
    private final KafkaDispatcher kafkaDispatcher;

    public void sendTransactionEvent(TransactionCustomerDto dto) {
        kafkaDispatcher.sendMessage(dto, TOPIC_TRANSACTION_EVENT, "create");
    }
}
