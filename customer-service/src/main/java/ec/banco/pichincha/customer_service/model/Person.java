package ec.banco.pichincha.customer_service.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private String gender;
    private int age;
    private String identification;
    private String address;
    private String phone;
}
