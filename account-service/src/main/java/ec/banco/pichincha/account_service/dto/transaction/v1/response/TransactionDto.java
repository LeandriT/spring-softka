package ec.banco.pichincha.account_service.dto.transaction.v1.response;

import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder()
public class TransactionDto extends BaseDto {
    private LocalDateTime date;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balance;
    private AccountDto account;
}