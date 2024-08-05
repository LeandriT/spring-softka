package ec.banco.pichincha.account_service.controller;

import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.dto.reports.v1.AccountStatementReport;
import ec.banco.pichincha.account_service.dto.retentions.OnCreate;
import ec.banco.pichincha.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService service;

    @GetMapping
    public ResponseEntity<Page<AccountDto>> index(Pageable pageable) {
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AccountDto> show(@PathVariable UUID uuid) {
        return new ResponseEntity<>(service.show(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@Validated(OnCreate.class) @RequestBody AccountRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<AccountDto> update(@PathVariable UUID uuid, @RequestBody AccountRequest request) {
        return new ResponseEntity<>(service.update(uuid, request), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/reports")
    public ResponseEntity<Page<AccountStatementReport>> reportV1(
            Pageable pageable,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("customerUuid") UUID customerUuid
    ) {
        Page<AccountStatementReport> accountStatementReports = service.accountStatementReport(pageable, customerUuid, this.toLocalDate(startDate), this.toLocalDate(endDate));
        return new ResponseEntity<>(accountStatementReports, HttpStatus.OK);
    }

    LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + e.getMessage());
            throw new RuntimeException("Error to read dates");
        }
    }
}
