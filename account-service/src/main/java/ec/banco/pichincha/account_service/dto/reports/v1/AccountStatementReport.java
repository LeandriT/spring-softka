package ec.banco.pichincha.account_service.dto.reports.v1;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementReport {
    private CustomerReport customer;
}
