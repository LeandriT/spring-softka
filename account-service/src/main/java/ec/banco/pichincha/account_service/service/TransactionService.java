package ec.banco.pichincha.account_service.service;

import ec.banco.pichincha.account_service.dto.transaction.v1.request.TransactionRequest;
import ec.banco.pichincha.account_service.dto.transaction.v1.response.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {
    TransactionDto show(UUID uuid);

    TransactionDto create(TransactionRequest request);

    TransactionDto update(UUID uuid, TransactionRequest request);

    void delete(UUID uuid);

    Page<TransactionDto> findAll(Pageable pageable);
}
