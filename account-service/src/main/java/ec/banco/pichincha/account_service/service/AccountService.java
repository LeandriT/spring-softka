package ec.banco.pichincha.account_service.service;

import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.dto.reports.v1.AccountStatementReport;
import ec.banco.pichincha.account_service.eventHandler.accountEventDto.AccountBalanceDto;
import ec.banco.pichincha.account_service.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface AccountService {
    AccountDto show(UUID uuid);

    AccountDto create(AccountRequest request);

    AccountDto update(UUID uuid, AccountRequest request);

    void delete(UUID uuid);

    Page<AccountDto> findAll(Pageable pageable);

    Account findByUuid(UUID uuid);

    void updateAccountBalance(AccountBalanceDto accountBalanceDto);

    Page<AccountStatementReport> accountStatementReport(
            Pageable pageable, UUID customerUuid, LocalDate startDate, LocalDate endDate
    );
}
