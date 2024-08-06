package ec.banco.pichincha.customer_service.service.impl;

import ec.banco.pichincha.customer_service.dto.customer.v1.request.CustomerRequest;
import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import ec.banco.pichincha.customer_service.exception.CustomerIdentificationFoundException;
import ec.banco.pichincha.customer_service.exception.CustomerIdentificationInvalidException;
import ec.banco.pichincha.customer_service.exception.CustomerNotFoundException;
import ec.banco.pichincha.customer_service.mapper.CustomerMapper;
import ec.banco.pichincha.customer_service.model.Customer;
import ec.banco.pichincha.customer_service.model.Person;
import ec.banco.pichincha.customer_service.repository.CustomerRepository;
import ec.banco.pichincha.customer_service.util.CedulaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequest customerRequest;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        customer = new Customer();
        customer.setUuid(uuid);
        customer.setPerson(Person.builder().name("John Doe").identification("1234567890").build());

        customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");
        customerRequest.setIdentification("1712345678");

        customerDto = new CustomerDto();
        customerDto.setName("John Doe");
        customerDto.setIdentification("1712345678");
    }

    @Test
    void show_CustomerExists_ReturnsCustomerDto() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto result = customerService.show(customer.getUuid());

        assertEquals(customerDto, result);
    }

    @Test
    void show_CustomerDoesNotExist_ThrowsCustomerNotFoundException() {
        UUID uuid = UUID.randomUUID();
        String message = String.format("Customer doest no exists %s", uuid);
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerService.show(uuid));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void create_ValidRequest_ReturnsCustomerDto() {
        String identification = customerRequest.getIdentification(); // Asegúrate de que esto coincida con el valor usado en el test

        // Simula el mapeo de CustomerRequest a Customer
        when(customerMapper.toEntity(any(CustomerRequest.class))).thenReturn(customer);

        // Simula que el identificador no existe en el repositorio
        when(repository.existsByPersonIdentification(identification)).thenReturn(false);

        // Usa MockedStatic para simular el validador de cédula
        try (MockedStatic<CedulaValidator> mockedCedulaValidator = mockStatic(CedulaValidator.class)) {
            mockedCedulaValidator.when(() -> CedulaValidator.isValidCedula(identification)).thenReturn(true);

            // Simula la operación de guardado en el repositorio
            when(repository.save(any(Customer.class))).thenReturn(customer);

            // Simula el mapeo de Customer a CustomerDto
            when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

            // Llama al método bajo prueba
            CustomerDto result = customerService.create(customerRequest);

            // Verifica el resultado
            assertEquals(customerDto, result);
        }
    }


    @Test
    void create_IdentificationExists_ThrowsCustomerIdentificationFoundException() {
        String identification = customerRequest.getIdentification();
        when(repository.existsByPersonIdentification(identification)).thenReturn(true);

        Exception exception = assertThrows(CustomerIdentificationFoundException.class, () -> customerService.create(customerRequest));

        assertEquals("customer with " + identification + " already exists", exception.getMessage());
    }

    @Test
    void create_InvalidIdentification_ThrowsCustomerIdentificationInvalidException() {
        String identification = customerRequest.getIdentification();
        when(repository.existsByPersonIdentification(identification)).thenReturn(false);

        try (MockedStatic<CedulaValidator> mockedCedulaValidator = mockStatic(CedulaValidator.class)) {
            mockedCedulaValidator.when(() -> CedulaValidator.isValidCedula(identification)).thenReturn(false);

            Exception exception = assertThrows(CustomerIdentificationInvalidException.class, () -> customerService.create(customerRequest));

            assertEquals("customer with " + identification + " is invalid", exception.getMessage());
        }
    }

    @Test
    void update_CustomerExists_ReturnsUpdatedCustomerDto() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerMapper.updateModel(any(CustomerRequest.class), any(Customer.class))).thenReturn(customer);
        when(repository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        CustomerDto result = customerService.update(customer.getUuid(), customerRequest);

        assertEquals(customerDto, result);
    }

    @Test
    void update_CustomerDoesNotExist_ThrowsCustomerNotFoundException() {
        UUID uuid = UUID.randomUUID();
        String message = String.format("Customer doest no exists %s", uuid);
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerService.update(uuid, customerRequest));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void delete_CustomerExists_DeletesCustomer() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        doNothing().when(repository).delete(any(Customer.class));

        customerService.delete(customer.getUuid());

        verify(repository, times(1)).delete(customer);
    }

    @Test
    void delete_CustomerDoesNotExist_ThrowsCustomerNotFoundException() {
        UUID uuid = UUID.randomUUID();
        String message = String.format("Customer doest no exists %s", uuid);
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerService.delete(uuid));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void findAll_ReturnsPageOfCustomerDto() {
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));
        Page<CustomerDto> customerDtoPage = new PageImpl<>(List.of(customerDto));

        when(repository.findAll(any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(customerDto);

        Page<CustomerDto> result = customerService.findAll(Pageable.unpaged());

        assertEquals(customerDtoPage.getContent(), result.getContent());
    }
}
