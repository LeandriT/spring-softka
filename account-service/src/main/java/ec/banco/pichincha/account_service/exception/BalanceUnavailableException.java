package ec.banco.pichincha.account_service.exception;

public class BalanceUnavailableException extends RuntimeException {

    public BalanceUnavailableException(String message) {
        super(message);
    }

}