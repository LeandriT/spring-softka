package ec.banco.pichincha.account_service.service.impl;

import ec.banco.pichincha.account_service.clients.customer.CustomerClient;
import ec.banco.pichincha.account_service.clients.customer.dto.CustomerDto;
import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.dto.reports.v1.AccountStatementReport;
import ec.banco.pichincha.account_service.dto.reports.v1.CustomerAccountStatementReport;
import ec.banco.pichincha.account_service.dto.reports.v1.CustomerReport;
import ec.banco.pichincha.account_service.dto.reports.v1.TransactionAccountStatementReport;
import ec.banco.pichincha.account_service.eventHandler.accountEventDto.AccountBalanceDto;
import ec.banco.pichincha.account_service.exception.AccountNotFoundException;
import ec.banco.pichincha.account_service.exception.CustomerNotFoundException;
import ec.banco.pichincha.account_service.mapper.AccountMapper;
import ec.banco.pichincha.account_service.model.Account;
import ec.banco.pichincha.account_service.model.Transaction;
import ec.banco.pichincha.account_service.repository.AccountRepository;
import ec.banco.pichincha.account_service.service.AccountService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final CustomerClient client;

    @Override
    public AccountDto show(UUID uuid) {
        String message = String.format("Account doest no exists %s", uuid);
        Account entity = repository.findById(uuid)
                .orElseThrow(() -> new AccountNotFoundException(message));
        return mapper.toDto(entity);
    }

    @Override
    public AccountDto create(AccountRequest request) {
        this.validateCustomerExists(request.getCustomerUuid());
        Account entity = mapper.toEntity(request);
        entity.setActualBalance(request.getInitialBalance());
        repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public AccountDto update(UUID uuid, AccountRequest request) {
        String message = String.format("Account doest no exists %s", uuid);
        Account entity = repository.findById(uuid).orElseThrow(() -> new AccountNotFoundException(message));
        entity = mapper.updateModel(request, entity);
        repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID uuid) {
        String message = String.format("Account doest no exists %s", uuid);
        Account entity = repository.findById(uuid).orElseThrow(() -> new AccountNotFoundException(message));
        repository.delete(entity);
    }

    @Override
    public Page<AccountDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Account findByUuid(UUID uuid) {
        String message = String.format("Account doest no exists %s", uuid);
        return repository.findById(uuid)
                .orElseThrow(() -> new AccountNotFoundException(message));
    }

    @Override
    public void updateAccountBalance(AccountBalanceDto accountBalanceDto) {
        String message = String.format("Account doest no exists %s", accountBalanceDto.getAccountUuid());
        Account account = repository.findById(accountBalanceDto.getAccountUuid())
                .orElseThrow(() -> new AccountNotFoundException(message));
        account.setActualBalance(accountBalanceDto.getBalance());
        repository.save(account);
    }

    @Override
    @Transactional
    public Page<AccountStatementReport> accountStatementReport(Pageable pageable, UUID customerUuid, LocalDate startDate, LocalDate endDate) {
        Page<Account> accountsPage = repository.findByCustomerUuidAndStartDateAndEndDate(
                pageable, customerUuid, dateToString(startDate.atStartOfDay()), dateToString(endDate.atTime(LocalTime.MAX))
        );
        this.validateCustomerExists(customerUuid);
        CustomerDto customerDto = client.show(customerUuid);
        CustomerReport customerReport = this.buildCustomerReport(customerDto);
        List<CustomerAccountStatementReport> accounts = accountsPage.getContent().stream().map(account -> CustomerAccountStatementReport.builder()
                .type(account.getType())
                .actualBalance(account.getActualBalance())
                .number(account.getNumber())
                .initialBalance(account.getInitialBalance())
                .transactions(
                        account.getTransactions().stream().map(this::buildTransactionAccountStatementReport)
                                .collect(Collectors.toSet())
                )
                .build()).toList();
        customerReport.setAccounts(accounts);
        AccountStatementReport accountStatementReport = AccountStatementReport.builder().customer(customerReport).build();
        List<AccountStatementReport> accountStatementReports = List.of(accountStatementReport);
        return new PageImpl<>(
                accountStatementReports, pageable, accountsPage.getTotalElements()
        );
    }

    String dateToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return dateTime.format(formatter);
    }

    void validateCustomerExists(UUID customerUuid) {
        try {
            client.show(customerUuid);
        } catch (CustomerNotFoundException | FeignException.NotFound e) {
            log.error("Error fetching customer with ID {}: {}", customerUuid, e.getMessage(), e);
            String message = String.format("Customer doest no exists %s", customerUuid);
            throw new CustomerNotFoundException(message);
        } catch (FeignException e) {
            throw new RuntimeException("An error occurred while fetching the customer", e);
        }
    }


    CustomerReport buildCustomerReport(CustomerDto dto) {
        return CustomerReport.builder()
                .name(dto.getName())
                .build();
    }

    TransactionAccountStatementReport buildTransactionAccountStatementReport(Transaction transaction) {
        return TransactionAccountStatementReport.builder()
                .date(transaction.getDate())
                .balance(transaction.getBalance())
                .build();
    }

}
