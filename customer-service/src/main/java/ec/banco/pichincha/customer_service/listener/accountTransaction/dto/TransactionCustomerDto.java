package ec.banco.pichincha.customer_service.listener.accountTransaction.dto;

import ec.banco.pichincha.customer_service.enums.TransactionTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionCustomerDto implements Serializable {
    private UUID customerUuid;
    private BigDecimal amount;
    private TransactionTypeEnum transactionType;
}
