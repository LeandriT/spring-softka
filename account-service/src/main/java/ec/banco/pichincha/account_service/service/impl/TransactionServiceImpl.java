package ec.banco.pichincha.account_service.service.impl;

import ec.banco.pichincha.account_service.dto.transaction.v1.request.TransactionRequest;
import ec.banco.pichincha.account_service.dto.transaction.v1.response.TransactionDto;
import ec.banco.pichincha.account_service.enums.TransactionTypeEnum;
import ec.banco.pichincha.account_service.eventHandler.accountEventDto.AccountBalanceDto;
import ec.banco.pichincha.account_service.exception.*;
import ec.banco.pichincha.account_service.mapper.TransactionMapper;
import ec.banco.pichincha.account_service.model.Account;
import ec.banco.pichincha.account_service.model.Transaction;
import ec.banco.pichincha.account_service.repository.TransactionRepository;
import ec.banco.pichincha.account_service.service.AccountService;
import ec.banco.pichincha.account_service.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final AccountService accountService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public TransactionDto show(UUID uuid) {
        String message = String.format("Transaction doest no exists %s", uuid);
        Transaction entity = repository.findById(uuid).orElseThrow(() -> new TransactionNotFoundException(message));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionRequest request) {
        this.validateTransactionType(request);
        Transaction entity = mapper.toEntity(request);
        this.buildAccount(entity, request.getAccountUuid());
        this.validateInsufficientFounds(entity.getAccount(), request);
        this.buildTransactionType(request, entity);
        repository.save(entity);
        AccountBalanceDto accountBalanceDto = this.buildAccountBalanceDto(entity.getAccount().getUuid(), entity.getBalance());
        applicationEventPublisher.publishEvent(accountBalanceDto);
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public TransactionDto update(UUID uuid, TransactionRequest request) {
        this.validateTransactionType(request);
        String message = String.format("Transaction doest no exists %s", uuid);
        Transaction entity = repository.findById(uuid).orElseThrow(() -> new TransactionNotFoundException(message));
        this.buildAccount(entity, request.getAccountUuid());
        this.validateInsufficientFounds(entity.getAccount(), request);
        this.buildTransactionType(request, entity);
        entity = mapper.updateModel(request, entity);
        repository.save(entity);
        AccountBalanceDto accountBalanceDto = this.buildAccountBalanceDto(entity.getAccount().getUuid(), entity.getBalance());
        applicationEventPublisher.publishEvent(accountBalanceDto);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID uuid) {
        String message = String.format("Transaction doest no exists %s", uuid);
        Transaction entity = repository.findById(uuid).orElseThrow(() -> new TransactionNotFoundException(message));
        repository.delete(entity);
    }

    @Override
    public Page<TransactionDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }


    void buildAccount(Transaction transaction, UUID uuid) {
        Account account = accountService.findByUuid(uuid);
        transaction.setAccount(account);
    }

    void validateTransactionType(TransactionRequest request) {
        if (Objects.equals(request.getTransactionType(), TransactionTypeEnum.DEPOSIT.name())) {
            boolean isNegative = request.getAmount().signum() == -1;
            if (isNegative) {
                String message = String.format("DEPOSIT negative amount %s invalid ", request.getAmount());
                throw new BalanceTypeSigNumUnavailableException(message);
            }
        } else if (Objects.equals(request.getTransactionType(), TransactionTypeEnum.WITHDRAWAL.name())) {
            boolean isNegative = request.getAmount().signum() == 1;
            if (isNegative) {
                String message = String.format("WITHDRAWAL positive amount %s invalid", request.getAmount());
                throw new BalanceTypeSigNumUnavailableException(message);
            }
        }
    }

    void buildTransactionType(TransactionRequest request, Transaction entity) {
        if (Objects.equals(request.getTransactionType(), TransactionTypeEnum.DEPOSIT.getDisplayName())) {
            entity.setBalance(entity.getAccount().getActualBalance().add(request.getAmount()));
        } else if (Objects.equals(request.getTransactionType(), TransactionTypeEnum.WITHDRAWAL.getDisplayName())) {
            entity.setBalance(entity.getAccount().getActualBalance().subtract(request.getAmount()));
        }
    }

    AccountBalanceDto buildAccountBalanceDto(UUID accountUuid, BigDecimal balance) {
        return new AccountBalanceDto(this, accountUuid, balance);
    }

    void validateInsufficientFounds(Account account, TransactionRequest request) {
        BigDecimal balance = account.getActualBalance();
        BigDecimal toWithdrawal = request.getAmount();
        if (Objects.equals(request.getTransactionType(), TransactionTypeEnum.WITHDRAWAL.getDisplayName())) {
            if (balance.subtract(toWithdrawal).signum() == -1) {
                String message = "Funds unavailable";
                throw new InsufficientFoundsException(message);
            }
        }

    }
}
