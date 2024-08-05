package ec.banco.pichincha.customer_service.dto.base.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import ec.banco.pichincha.customer_service.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public abstract class PersonResponseDto extends BaseDto {
    private String name;
    private String gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
