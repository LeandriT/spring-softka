package ec.banco.pichincha.customer_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.banco.pichincha.customer_service.dto.customer.v1.request.CustomerRequest;
import ec.banco.pichincha.customer_service.dto.customer.v1.response.CustomerDto;
import ec.banco.pichincha.customer_service.service.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void testGetIndex() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        when(customerService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        mockMvc.perform(get("/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        UUID uuid = UUID.randomUUID();
        CustomerDto customerDto = this.buildCustomerDto();
        customerDto.setUuid(uuid);
        when(customerService.show(uuid)).thenReturn(customerDto);

        mockMvc.perform(get("/customers/{uuid}", uuid)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(uuid.toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testCreate() throws Exception {
        CustomerRequest customerRequest = this.buildCustomerRequest();
        CustomerDto customerDto = this.buildCustomerDto();

        when(customerService.create(any(CustomerRequest.class))).thenReturn(customerDto);

        mockMvc.perform(post("/customers")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(customerDto.getUuid().toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testUpdate() throws Exception {
        UUID uuid = UUID.randomUUID();
        CustomerRequest customerRequest = new CustomerRequest();
        CustomerDto customerDto = this.buildCustomerDto();
        customerDto.setUuid(uuid);
        when(customerService.update(eq(uuid), any(CustomerRequest.class))).thenReturn(customerDto);

        mockMvc.perform(patch("/customers/{uuid}", uuid)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(customerDto.getUuid().toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(customerService).delete(uuid);

        mockMvc.perform(delete("/customers/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }

    private CustomerRequest buildCustomerRequest() {
        CustomerRequest object = new CustomerRequest();
        object.setName("JUAN");
        object.setGender("MALE");
        object.setAge(12);
        object.setIdentification("1703256897");
        object.setAddress("QUITO");
        object.setPhone("");
        return object;
    }

    private CustomerDto buildCustomerDto() {
        CustomerDto object = new CustomerDto();
        object.setUuid(UUID.randomUUID());
        object.setName("JUAN");
        object.setGender("MALE");
        object.setAge(12);
        object.setIdentification("1703256897");
        object.setAddress("QUITO");
        object.setPhone("");
        return object;
    }
}