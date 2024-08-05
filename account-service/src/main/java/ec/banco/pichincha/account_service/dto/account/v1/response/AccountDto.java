package ec.banco.pichincha.account_service.dto.account.v1.response;

import ec.banco.pichincha.account_service.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder()
public class AccountDto extends BaseDto {
    private String number;
    private String type;
    private BigDecimal initialBalance;
    private BigDecimal actualBalance;
    private Boolean status;
    private UUID customerUuid;
}
