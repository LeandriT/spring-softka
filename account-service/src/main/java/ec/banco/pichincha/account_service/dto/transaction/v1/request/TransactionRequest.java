package ec.banco.pichincha.account_service.dto.transaction.v1.request;

import ec.banco.pichincha.account_service.dto.retentions.OnCreate;
import ec.banco.pichincha.account_service.dto.retentions.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotNull(message = "date cannot be null", groups = {OnCreate.class})
    private LocalDateTime date;
    @NotNull(message = "transactionType cannot be null", groups = {OnCreate.class})
    private String transactionType;
    @NotNull(message = "amount cannot be null", groups = {OnCreate.class})
    private BigDecimal amount;
    @NotNull(message = "account uuid cannot be null", groups = {OnCreate.class, OnUpdate.class})
    private UUID accountUuid;
}
