package ec.banco.pichincha.customer_service.dto.customer.v1.request;

import ec.banco.pichincha.customer_service.dto.base.request.PersonRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CustomerRequest extends PersonRequest {
    private String clientId;
    private String password;
    private Boolean status;
}
