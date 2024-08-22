package ec.banco.pichincha.account_service.clients.customer;

import ec.banco.pichincha.account_service.clients.customer.dto.CustomerDto;
import ec.banco.pichincha.account_service.exception.CustomerNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "customer-client", url = "${app.ms.customer-service.url}")
public interface CustomerClient {

    @GetMapping("/customers/{uuid}")
    @CircuitBreaker(name = "customerClientCircuit", fallbackMethod = "fallbackDataById")
    @Retry(name = "customerClientRetry")
    CustomerDto show(@PathVariable("uuid") UUID uuid);


    default CustomerDto fallbackDataById(UUID uuid, Throwable throwable) {
        throw new CustomerNotFoundException("Customer service is unavailable. Error: " + throwable.getMessage());
    }
}
