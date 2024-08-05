package ec.banco.pichincha.account_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.service.AccountService;
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

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    @InjectMocks
    private AccountController customerController;
    String path = "/accounts";

    @Test
    void testGetIndex() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        when(service.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        mockMvc.perform(get(path)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testShow() throws Exception {
        UUID uuid = UUID.randomUUID();
        AccountDto customerDto = this.buildDto();
        customerDto.setUuid(uuid);
        when(service.show(uuid)).thenReturn(customerDto);

        mockMvc.perform(get(path + "/{uuid}", uuid)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(uuid.toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testCreate() throws Exception {
        AccountRequest request = this.buildRequest();
        AccountDto customerDto = this.buildDto();

        when(service.create(any(AccountRequest.class))).thenReturn(customerDto);

        mockMvc.perform(post(path)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(customerDto.getUuid().toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testUpdate() throws Exception {
        UUID uuid = UUID.randomUUID();
        AccountRequest customerRequest = new AccountRequest();
        AccountDto dto = this.buildDto();
        dto.setUuid(uuid);
        when(service.update(eq(uuid), any(AccountRequest.class))).thenReturn(dto);

        mockMvc.perform(patch(path + "/{uuid}", uuid)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(dto.getUuid().toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testDelete() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(service).delete(uuid);

        mockMvc.perform(delete(path + "/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }

    private AccountRequest buildRequest() {
        AccountRequest object = new AccountRequest();
        object.setInitialBalance(BigDecimal.valueOf(100));
        object.setNumber(new SecureRandom().nextInt(10) + "");
        object.setStatus(true);
        object.setType("AHORROS");
        return object;
    }

    private AccountDto buildDto() {
        AccountDto object = new AccountDto();
        object.setUuid(UUID.randomUUID());
        object.setActualBalance(BigDecimal.valueOf(100));
        object.setInitialBalance(BigDecimal.ZERO);
        object.setNumber(new SecureRandom().nextInt(10) + "");
        return object;
    }
}