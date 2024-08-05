package ec.banco.pichincha.customer_service.model;

import ec.banco.pichincha.customer_service.model.abstracts.BaseModel;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "customers")
@SuperBuilder
@SQLDelete(sql = "UPDATE customers SET is_deleted = true, deleted_at=now() WHERE uuid=?")
@Where(clause = "is_deleted is false")
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseModel implements Serializable {
    @Embedded
    private Person person;
    private String clientId;
    private String password;
    private Boolean status;
}
