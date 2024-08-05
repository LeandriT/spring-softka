package ec.banco.pichincha.account_service.clients.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto implements Serializable {
    private UUID uuid;
    private String name;
    private Boolean status;
}
