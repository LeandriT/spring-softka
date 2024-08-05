package ec.banco.pichincha.account_service.eventHandler;

import ec.banco.pichincha.account_service.eventHandler.accountEventDto.AccountBalanceDto;
import ec.banco.pichincha.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountBalanceEventListener implements ApplicationListener<AccountBalanceDto> {
    private final AccountService service;

    @EventListener
    public void handleAccountBalance(AccountBalanceDto event) {
        System.out.println("Received event with data: " + event.toString());
        service.updateAccountBalance(event);
    }

    @Override
    public void onApplicationEvent(AccountBalanceDto event) {
        System.out.println("Received onApplicationEvent");
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
