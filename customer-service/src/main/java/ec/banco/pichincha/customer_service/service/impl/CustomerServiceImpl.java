package ec.banco.pichincha.customer_service.service.impl;

import ec.banco.pichincha.customer_service.dto.customer.v1.request.CustomerRequest;
import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import ec.banco.pichincha.customer_service.exception.CustomerIdentificationFoundException;
import ec.banco.pichincha.customer_service.exception.CustomerIdentificationInvalidException;
import ec.banco.pichincha.customer_service.exception.CustomerNotFoundException;
import ec.banco.pichincha.customer_service.mapper.CustomerMapper;
import ec.banco.pichincha.customer_service.model.Customer;
import ec.banco.pichincha.customer_service.repository.CustomerRepository;
import ec.banco.pichincha.customer_service.service.CustomerService;
import ec.banco.pichincha.customer_service.util.CedulaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto show(UUID uuid) {
        String message = String.format("Customer doest no exists %s", uuid);
        Customer customer = repository.findById(uuid)
                .orElseThrow(() -> new CustomerNotFoundException(message));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto create(CustomerRequest request) {
        Customer entity = customerMapper.toEntity(request);
        this.validate(request);
        repository.save(entity);
        return customerMapper.toDto(entity);
    }

    @Override
    public CustomerDto update(UUID uuid, CustomerRequest request) {
        String message = String.format("Customer doest no exists %s", uuid);
        Customer entity = repository.findById(uuid).orElseThrow(() -> new CustomerNotFoundException(message));
        entity = customerMapper.updateModel(request, entity);
        repository.save(entity);
        return customerMapper.toDto(entity);
    }

    @Override
    public void delete(UUID uuid) {
        String message = String.format("Customer doest no exists %s", uuid);
        Customer entity = repository.findById(uuid).orElseThrow(() -> new CustomerNotFoundException(message));
        repository.delete(entity);
    }

    @Override
    public Page<CustomerDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(customerMapper::toDto);
    }

    void validateIdentificationExists(CustomerRequest request) {
        boolean exists = repository.existsByPersonIdentification(request.getIdentification());
        if (exists) {
            String message = String.format("customer with %s already exists", request.getIdentification());
            throw new CustomerIdentificationFoundException(message);
        }
    }

    void validateIdentificationEc(CustomerRequest request) {
        boolean validCedula = CedulaValidator.isValidCedula(request.getIdentification());
        if (!validCedula) {
            String message = String.format("customer with %s is invalid", request.getIdentification());
            throw new CustomerIdentificationInvalidException(message);
        }
    }

    void validate(CustomerRequest request) {
        this.validateIdentificationExists(request);
        this.validateIdentificationEc(request);
    }

}
