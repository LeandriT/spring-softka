package ec.banco.pichincha.customer_service.service;

import ec.banco.pichincha.customer_service.dto.customer.v1.request.CustomerRequest;
import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {
    CustomerDto show(UUID uuid);

    CustomerDto create(CustomerRequest request);

    CustomerDto update(UUID uuid, CustomerRequest request);

    void delete(UUID uuid);

    Page<CustomerDto> findAll(Pageable pageable);
}
