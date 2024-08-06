package ec.banco.pichincha.account_service.service.impl;

import ec.banco.pichincha.account_service.clients.customer.CustomerClient;
import ec.banco.pichincha.account_service.clients.customer.dto.CustomerDto;
import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.eventHandler.accountEventDto.AccountBalanceDto;
import ec.banco.pichincha.account_service.exception.AccountNotFoundException;
import ec.banco.pichincha.account_service.exception.CustomerNotFoundException;
import ec.banco.pichincha.account_service.mapper.AccountMapper;
import ec.banco.pichincha.account_service.model.Account;
import ec.banco.pichincha.account_service.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @Mock
    private CustomerClient client;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountRequest accountRequest;
    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUuid(UUID.randomUUID());
        account.setNumber("1234567890");
        account.setType("AHORROS");
        account.setInitialBalance(BigDecimal.valueOf(100.00));
        account.setActualBalance(BigDecimal.valueOf(11099.98));
        accountRequest = new AccountRequest();
        accountRequest.setNumber("1234567890");
        accountRequest.setType("AHORROS");
        accountRequest.setInitialBalance(BigDecimal.valueOf(100.00));
        accountRequest.setStatus(true);
        accountRequest.setCustomerUuid(account.getCustomerUuid());
        accountDto = new AccountDto();
        accountDto.setNumber("1234567890");
        accountDto.setType("AHORROS");
        accountDto.setInitialBalance(BigDecimal.valueOf(100.00));
        accountDto.setActualBalance(BigDecimal.valueOf(11099.98));
    }

    @Test
    void show_Success() {
        UUID uuid = account.getUuid();
        when(repository.findById(uuid)).thenReturn(Optional.of(account));
        when(mapper.toDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.show(uuid);

        assertEquals(accountDto, result);
    }

    @Test
    void show_AccountNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> accountService.show(uuid));

        assertEquals("Account doest no exists " + uuid, exception.getMessage());
    }

    @Test
    void create_Success() {
        when(mapper.toEntity(accountRequest)).thenReturn(account);
        when(repository.save(account)).thenReturn(account);
        when(mapper.toDto(account)).thenReturn(accountDto);
        when(client.show(accountRequest.getCustomerUuid())).thenReturn(new CustomerDto());

        AccountDto result = accountService.create(accountRequest);

        assertEquals(accountDto, result);
        verify(repository).save(account);
    }

    @Test
    void create_CustomerNotFound() {
        when(client.show(accountRequest.getCustomerUuid())).thenThrow(new CustomerNotFoundException("Customer not found"));

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> accountService.create(accountRequest));

        assertEquals("Customer doest no exists " + accountRequest.getCustomerUuid(), exception.getMessage());
    }

    @Test
    void update_Success() {
        UUID uuid = account.getUuid();
        when(repository.findById(uuid)).thenReturn(Optional.of(account));
        when(mapper.updateModel(accountRequest, account)).thenReturn(account);
        when(repository.save(account)).thenReturn(account);
        when(mapper.toDto(account)).thenReturn(accountDto);
        when(client.show(accountRequest.getCustomerUuid())).thenReturn(new CustomerDto());

        AccountDto result = accountService.update(uuid, accountRequest);

        assertEquals(accountDto, result);
        verify(repository).save(account);
    }

    @Test
    void update_AccountNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> accountService.update(uuid, accountRequest));

        assertEquals("Account doest no exists " + uuid, exception.getMessage());
    }

    @Test
    void delete_Success() {
        UUID uuid = account.getUuid();
        when(repository.findById(uuid)).thenReturn(Optional.of(account));

        accountService.delete(uuid);

        verify(repository).delete(account);
    }

    @Test
    void delete_AccountNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> accountService.delete(uuid));

        assertEquals("Account doest no exists " + uuid, exception.getMessage());
    }

    @Test
    void findAll_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account), pageable, 1);
        Page<AccountDto> accountDtoPage = new PageImpl<>(Collections.singletonList(accountDto), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(accountPage);
        when(mapper.toDto(account)).thenReturn(accountDto);

        Page<AccountDto> result = accountService.findAll(pageable);

        assertEquals(accountDtoPage, result);
    }

    @Test
    void updateAccountBalance_Success() {
        AccountBalanceDto accountBalanceDto = new AccountBalanceDto(this, UUID.randomUUID(), BigDecimal.valueOf(100));
        accountBalanceDto.setAccountUuid(account.getUuid());
        accountBalanceDto.setBalance(BigDecimal.valueOf(12000.00));

        when(repository.findById(account.getUuid())).thenReturn(Optional.of(account));

        accountService.updateAccountBalance(accountBalanceDto);

        assertEquals(BigDecimal.valueOf(12000.00), account.getActualBalance());
        verify(repository).save(account);
    }

    @Test
    void updateAccountBalance_AccountNotFound() {
        AccountBalanceDto accountBalanceDto = new AccountBalanceDto(this, UUID.randomUUID(), BigDecimal.valueOf(100));

        when(repository.findById(accountBalanceDto.getAccountUuid())).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> accountService.updateAccountBalance(accountBalanceDto));

        assertEquals("Account doest no exists " + accountBalanceDto.getAccountUuid(), exception.getMessage());
    }

}