package ec.banco.pichincha.account_service.dto.account.v1.request;

import ec.banco.pichincha.account_service.dto.retentions.OnCreate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @NotNull(message = "number cannot be null", groups = {OnCreate.class})
    private String number;
    @NotNull(message = "type cannot be null", groups = {OnCreate.class})
    private String type;
    @Min(value = 1, message = "initial balance must be greater than 0", groups = {OnCreate.class})
    private BigDecimal initialBalance;
    @NotNull(message = "status cannot be null", groups = {OnCreate.class})
    private Boolean status;
    @NotNull(message = "customer_uuid cannot be null", groups = {OnCreate.class})
    private UUID customerUuid;
}
