package ec.banco.pichincha.account_service.service.impl;

import ec.banco.pichincha.account_service.dto.transaction.v1.request.TransactionRequest;
import ec.banco.pichincha.account_service.dto.transaction.v1.response.TransactionDto;
import ec.banco.pichincha.account_service.enums.TransactionTypeEnum;
import ec.banco.pichincha.account_service.eventHandler.accountEventDto.AccountBalanceDto;
import ec.banco.pichincha.account_service.exception.BalanceTypeSigNumUnavailableException;
import ec.banco.pichincha.account_service.exception.InsufficientFoundsException;
import ec.banco.pichincha.account_service.exception.TransactionNotFoundException;
import ec.banco.pichincha.account_service.mapper.TransactionMapper;
import ec.banco.pichincha.account_service.model.Account;
import ec.banco.pichincha.account_service.model.Transaction;
import ec.banco.pichincha.account_service.repository.TransactionRepository;
import ec.banco.pichincha.account_service.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private AccountService accountService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private TransactionRequest transactionRequest;
    private TransactionDto transactionDto;
    private Account account;

    @BeforeEach
    void setUp() {
        // Inicializa las instancias
        account = new Account();
        transaction = new Transaction();
        transactionRequest = new TransactionRequest();
        transactionDto = new TransactionDto();

        // Establece valores para la cuenta
        UUID accountUuid = UUID.randomUUID();
        account.setUuid(accountUuid);
        account.setActualBalance(BigDecimal.valueOf(500.00));

        // Establece valores para la transacción
        transaction.setUuid(UUID.randomUUID());
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.valueOf(100.00));
        transaction.setBalance(BigDecimal.valueOf(100.00));

        // Establece valores para la solicitud de transacción
        transactionRequest.setAccountUuid(accountUuid); // Usa el UUID de cuenta establecido
        transactionRequest.setAmount(BigDecimal.valueOf(100.00));
        transactionRequest.setTransactionType(TransactionTypeEnum.DEPOSIT.name());

        // Establece valores para el DTO de transacción
        transactionDto.setAmount(BigDecimal.valueOf(100.00));
        transactionDto.setBalance(BigDecimal.valueOf(100.00));
    }

    @Test
    void show_Success() {
        UUID uuid = transaction.getUuid();
        when(repository.findById(uuid)).thenReturn(Optional.of(transaction));
        when(mapper.toDto(transaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.show(uuid);

        assertEquals(transactionDto, result);
    }

    @Test
    void show_TransactionNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.show(uuid));

        assertEquals("Transaction doest no exists " + uuid, exception.getMessage());
    }

    @Test
    void create_Success() {
        when(mapper.toEntity(transactionRequest)).thenReturn(transaction);
        when(accountService.findByUuid(transactionRequest.getAccountUuid())).thenReturn(account);
        when(mapper.toDto(transaction)).thenReturn(transactionDto);
        when(repository.save(transaction)).thenReturn(transaction);

        TransactionDto result = transactionService.create(transactionRequest);

        assertEquals(transactionDto, result);
        verify(repository).save(transaction);
        verify(applicationEventPublisher).publishEvent(any(AccountBalanceDto.class));
    }

    @Test
    void create_InvalidDepositAmount() {
        transactionRequest.setTransactionType(TransactionTypeEnum.DEPOSIT.name());
        transactionRequest.setAmount(BigDecimal.valueOf(-100.00));

        // Lanza una excepción al llamar al método create
        Exception exception = assertThrows(BalanceTypeSigNumUnavailableException.class, () -> transactionService.create(transactionRequest));

        // Verifica que el mensaje de la excepción sea el esperado
        assertEquals("DEPOSIT negative amount -100.0 invalid ", exception.getMessage());
    }

    @Test
    void create_InvalidWithdrawalAmount() {
        transactionRequest.setTransactionType(TransactionTypeEnum.WITHDRAWAL.getDisplayName());
        transactionRequest.setAmount(BigDecimal.valueOf(5000));
        account.setActualBalance(BigDecimal.valueOf(50.00));

        when(mapper.toEntity(transactionRequest)).thenReturn(transaction);
        when(accountService.findByUuid(transactionRequest.getAccountUuid())).thenReturn(account);

        Exception exception = assertThrows(InsufficientFoundsException.class, () -> transactionService.create(transactionRequest));

        assertEquals("Funds unavailable", exception.getMessage());
    }

    @Test
    void update_Success() {
        UUID uuid = transaction.getUuid();
        when(repository.findById(uuid)).thenReturn(Optional.of(transaction));
        when(mapper.updateModel(transactionRequest, transaction)).thenReturn(transaction);
        when(accountService.findByUuid(transactionRequest.getAccountUuid())).thenReturn(account);
        when(mapper.toDto(transaction)).thenReturn(transactionDto);
        when(repository.save(transaction)).thenReturn(transaction);

        TransactionDto result = transactionService.update(uuid, transactionRequest);

        assertEquals(transactionDto, result);
        verify(repository).save(transaction);
        verify(applicationEventPublisher).publishEvent(any(AccountBalanceDto.class));
    }

    @Test
    void update_TransactionNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.update(uuid, transactionRequest));

        assertEquals("Transaction doest no exists " + uuid, exception.getMessage());
    }

    @Test
    void delete_Success() {
        UUID uuid = transaction.getUuid();
        when(repository.findById(uuid)).thenReturn(Optional.of(transaction));

        transactionService.delete(uuid);

        verify(repository).delete(transaction);
    }

    @Test
    void delete_TransactionNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.delete(uuid));

        assertEquals("Transaction doest no exists " + uuid, exception.getMessage());
    }

    @Test
    void findAll_Success() {
        Pageable pageable = Pageable.ofSize(10);
        Page<Transaction> transactionPage = new PageImpl<>(Collections.singletonList(transaction), pageable, 1);
        Page<TransactionDto> transactionDtoPage = new PageImpl<>(Collections.singletonList(transactionDto), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(transactionPage);
        when(mapper.toDto(transaction)).thenReturn(transactionDto);

        Page<TransactionDto> result = transactionService.findAll(pageable);

        assertEquals(transactionDtoPage, result);
    }
}
