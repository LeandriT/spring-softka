package ec.banco.pichincha.customer_service.service.impl;

import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import ec.banco.pichincha.customer_service.exception.CustomerNotFoundException;
import ec.banco.pichincha.customer_service.mapper.CustomerMapper;
import ec.banco.pichincha.customer_service.model.Customer;
import ec.banco.pichincha.customer_service.model.Person;
import ec.banco.pichincha.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CustomerServiceImplTest {
    @InjectMocks
    private CustomerServiceImpl service;

    @Mock
    private CustomerRepository repository;
    @Mock
    private CustomerMapper mapper;

    @Test()
    void happyPathShow() {
        //arrange
        UUID uuid = UUID.randomUUID();
        Customer customer = this.buildCustomer();
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(mapper.toDto(customer)).thenReturn(this.buildCustomerDto(customer));
        //act
        CustomerDto customerDto = service.show(uuid);
        //assert
        assertThat(customerDto.getName()).isEqualTo(customer.getPerson().getName());
    }

    @Test
    void whenUserNotFoundThenReturnThrowError() {
        //arrange
        UUID uuid = UUID.randomUUID();
        String expectedMessage = String.format("Customer doest no exists %s", uuid);
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        //act - assert

        CustomerNotFoundException customerNotFoundException = assertThrows(CustomerNotFoundException.class, () -> service.show(uuid));

        String actualMessage = customerNotFoundException.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    Customer buildCustomer() {
        Person build = Person.builder()
                .name("Juan")
                .gender("Hombre")
                .age(23)
                .identification("1701232345")
                .address("QUITO")
                .phone("0912234567")
                .build();

        return Customer.builder()
                .clientId(UUID.randomUUID().toString())
                .uuid(UUID.randomUUID())
                .person(build)
                .build();
    }


    CustomerDto buildCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .clientId(customer.getClientId())
                .uuid(customer.getUuid())
                .name(customer.getPerson().getName())
                .gender(customer.getPerson().getGender())
                .age(customer.getPerson().getAge())
                .identification(customer.getPerson().getIdentification())
                .address(customer.getPerson().getAddress())
                .phone(customer.getPerson().getPhone())
                .build();
    }


}