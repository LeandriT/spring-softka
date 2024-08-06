package ec.banco.pichincha.account_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.banco.pichincha.account_service.dto.account.v1.request.AccountRequest;
import ec.banco.pichincha.account_service.dto.account.v1.response.AccountDto;
import ec.banco.pichincha.account_service.dto.reports.v1.AccountStatementReport;
import ec.banco.pichincha.account_service.dto.reports.v1.CustomerReport;
import ec.banco.pichincha.account_service.service.AccountService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    @Autowired
    private ObjectMapper objectMapper;

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
                        .content(objectMapper.writeValueAsString(request)))
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

    @Test
    void reportV1_ReturnsAccountStatementReports() throws Exception {
        // Datos de prueba
        UUID customerUuid = UUID.randomUUID();
        String startDate = "2023-07-01";
        String endDate = "2023-07-31";
        Pageable pageable = Pageable.unpaged();

        CustomerReport customerReport = new CustomerReport();
        customerReport.setName("John Doe");

        AccountStatementReport accountStatementReport = AccountStatementReport.builder()
                .customer(customerReport)
                .build();

        Page<AccountStatementReport> accountStatementReports = new PageImpl<>(
                Collections.singletonList(accountStatementReport), pageable, 1);

        // Mockear la respuesta del servicio
        Mockito.when(service.accountStatementReport(any(Pageable.class), any(UUID.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(accountStatementReports);

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(get(path + "/reports")
                        .param("customerUuid", customerUuid.toString())
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(accountStatementReports)));
    }

    private AccountRequest buildRequest() {
        AccountRequest object = new AccountRequest();
        object.setInitialBalance(BigDecimal.valueOf(100));
        object.setNumber(new SecureRandom().nextInt(10) + "");
        object.setStatus(true);
        object.setType("AHORROS");
        object.setCustomerUuid(UUID.randomUUID());
        return object;
    }

    private AccountDto buildDto() {
        AccountDto object = new AccountDto();
        object.setUuid(UUID.randomUUID());
        object.setActualBalance(BigDecimal.valueOf(100));
        object.setInitialBalance(BigDecimal.ZERO);
        object.setNumber(new SecureRandom().nextInt(10) + "");
        object.setCustomerUuid(UUID.randomUUID());
        return object;
    }
}