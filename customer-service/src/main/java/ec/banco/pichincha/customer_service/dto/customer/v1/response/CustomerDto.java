package ec.banco.pichincha.customer_service.dto.customer.v1.response;

import ec.banco.pichincha.customer_service.dto.base.response.PersonResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder()
public class CustomerDto extends PersonResponseDto {
    private String clientId;
    private String password;
    private Boolean status;
}
