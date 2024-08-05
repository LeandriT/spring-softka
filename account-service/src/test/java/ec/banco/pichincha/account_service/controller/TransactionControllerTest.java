package ec.banco.pichincha.account_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ec.banco.pichincha.account_service.dto.transaction.v1.request.TransactionRequest;
import ec.banco.pichincha.account_service.dto.transaction.v1.response.TransactionDto;
import ec.banco.pichincha.account_service.service.TransactionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;
    String path = "/transactions";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

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
        TransactionDto dto = this.buildDto();
        dto.setUuid(uuid);
        when(service.show(uuid)).thenReturn(dto);

        mockMvc.perform(get(path + "/{uuid}", uuid)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(uuid.toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testCreate() throws Exception {
        TransactionRequest request = this.buildRequest();
        TransactionDto dto = this.buildDto();

        when(service.create(any(TransactionRequest.class))).thenReturn(dto);

        mockMvc.perform(post(path)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(Matchers.equalTo(dto.getUuid().toString()))); // Adjust the jsonPath as per your DTO
    }

    @Test
    public void testUpdate() throws Exception {
        UUID uuid = UUID.randomUUID();
        TransactionRequest request = new TransactionRequest();
        TransactionDto dto = this.buildDto();
        dto.setUuid(uuid);
        when(service.update(eq(uuid), any(TransactionRequest.class))).thenReturn(dto);

        mockMvc.perform(patch(path + "/{uuid}", uuid)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(request)))
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

    private TransactionRequest buildRequest() {
        TransactionRequest object = new TransactionRequest();
        object.setDate(LocalDateTime.now());
        object.setTransactionType("DEPOSIT");
        object.setAmount(new BigDecimal("1000.00"));
        object.setAccountUuid(UUID.randomUUID());
        return object;
    }

    private TransactionDto buildDto() {
        TransactionDto object = new TransactionDto();
        object.setUuid(UUID.randomUUID());
        object.setDate(LocalDateTime.now());
        object.setTransactionType("WITHDRAWAL");
        object.setAmount(new BigDecimal("500.00"));
        object.setBalance(new BigDecimal("1500.00"));
        return object;
    }
}