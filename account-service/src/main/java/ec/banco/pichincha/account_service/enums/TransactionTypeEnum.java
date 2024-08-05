package ec.banco.pichincha.account_service.enums;

import lombok.Getter;

@Getter
public enum TransactionTypeEnum {
    DEPOSIT("DEPOSITO"),
    WITHDRAWAL("RETIRO");

    private final String displayName;

    TransactionTypeEnum(String displayName) {
        this.displayName = displayName;
    }

}
