package ec.banco.pichincha.account_service.dto.reports.v1;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAccountStatementReport {
    private BigDecimal balance;
    private LocalDateTime date;
}
