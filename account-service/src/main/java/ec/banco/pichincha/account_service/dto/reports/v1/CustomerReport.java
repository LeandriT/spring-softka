package ec.banco.pichincha.account_service.dto.reports.v1;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReport {
    private String name;
    @Builder.Default
    private List<CustomerAccountStatementReport> accounts = new ArrayList<>();
}
