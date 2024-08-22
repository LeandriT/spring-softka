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

    public static TransactionTypeEnum fromDisplayName(String displayName) {
        for (TransactionTypeEnum type : TransactionTypeEnum.values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with displayName " + displayName);
    }
}
