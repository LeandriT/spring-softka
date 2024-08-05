package ec.banco.pichincha.account_service.eventHandler.accountEventDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString(of = "accountUuid")
public class AccountBalanceDto extends ApplicationEvent implements Serializable {
    private UUID accountUuid;
    private BigDecimal balance;

    public AccountBalanceDto(Object source, UUID accountUuid, BigDecimal balance) {
        super(source);
        this.accountUuid = accountUuid;
        this.balance = balance;
    }
}
