package ec.banco.pichincha.customer_service.listener.accountTransaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.banco.pichincha.customer_service.config.kafka.KafkaMessage;
import ec.banco.pichincha.customer_service.listener.accountTransaction.dto.TransactionCustomerDto;
import ec.banco.pichincha.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static ec.banco.pichincha.customer_service.listener.topics.Topics.TOPIC_TRANSACTION_EVENT;

@Component
@RequiredArgsConstructor
public class CustomerAccountTransactionListener {

    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(CustomerAccountTransactionListener.class);
    private final CustomerService service;

    @KafkaListener(
            topics = TOPIC_TRANSACTION_EVENT,
            containerFactory = "config.kafka.consumerFactory"
    )
    public void kafkaConsumer(@Payload KafkaMessage message) {
        try {
            TransactionCustomerDto dto = objectMapper.convertValue(
                    message.getData(), TransactionCustomerDto.class);
            service.eventTransactionAccountProcessed(dto);
        } catch (IllegalArgumentException ilarex) {
            log.error("Error al consumir topic : {}", ilarex.getMessage(), ilarex);
        } catch (Throwable th) {
            log.error("Error al consumir topic th: {}", th.getMessage(), th);
        }
    }
}
