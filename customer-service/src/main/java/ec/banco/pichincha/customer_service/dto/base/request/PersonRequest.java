package ec.banco.pichincha.customer_service.dto.base.request;

import ec.banco.pichincha.customer_service.dto.retentions.OnCreate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class PersonRequest {

    @NotNull(message = "Name cannot be null", groups = {OnCreate.class})
    private String name;
    @NotNull(message = "Phone cannot be null", groups = {OnCreate.class})
    private String gender;
    @Min(value = 1, message = "Age must be greater than 0", groups = {OnCreate.class})
    private Integer age;
    @NotNull(message = "Identification cannot be null", groups = {OnCreate.class})
    private String identification;
    @NotNull(message = "Address cannot be null", groups = {OnCreate.class})
    private String address;
    @NotNull(message = "Phone cannot be null", groups = {OnCreate.class})
    private String phone;
}
