package ec.banco.pichincha.account_service.controller;

import ec.banco.pichincha.account_service.dto.retentions.OnCreate;
import ec.banco.pichincha.account_service.dto.transaction.v1.request.TransactionRequest;
import ec.banco.pichincha.account_service.dto.transaction.v1.response.TransactionDto;
import ec.banco.pichincha.account_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService service;

    @GetMapping
    public ResponseEntity<Page<TransactionDto>> index(Pageable pageable) {
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TransactionDto> show(@PathVariable UUID uuid) {
        return new ResponseEntity<>(service.show(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@Validated(OnCreate.class) @RequestBody TransactionRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<TransactionDto> update(@PathVariable UUID uuid, @RequestBody TransactionRequest request) {
        return new ResponseEntity<>(service.update(uuid, request), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
