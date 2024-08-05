package ec.banco.pichincha.account_service.dto.reports.v1;

import lombok.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountStatementReport {
    private String number;
    private String type;
    private BigDecimal initialBalance;
    private BigDecimal actualBalance;
    @Builder.Default
    private Set<TransactionAccountStatementReport> transactions = new TreeSet<>(Comparator.comparing(TransactionAccountStatementReport::getDate));

}
