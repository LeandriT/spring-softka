package ec.banco.pichincha.customer_service.controller;

import ec.banco.pichincha.customer_service.dto.customer.v1.request.CustomerRequest;
import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import ec.banco.pichincha.customer_service.dto.retentions.OnCreate;
import ec.banco.pichincha.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> index(Pageable pageable) {
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CustomerDto> show(@PathVariable UUID uuid) {
        return new ResponseEntity<>(service.show(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@Validated(OnCreate.class) @RequestBody CustomerRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<CustomerDto> update(@PathVariable UUID uuid, @RequestBody CustomerRequest request) {
        return new ResponseEntity<>(service.update(uuid, request), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        service.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
