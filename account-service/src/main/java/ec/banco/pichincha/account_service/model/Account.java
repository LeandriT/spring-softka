package ec.banco.pichincha.account_service.model;

import ec.banco.pichincha.account_service.model.abstracts.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET is_deleted = true, deleted_at=now() WHERE uuid=?")
@Where(clause = "is_deleted is false")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseModel {
    private String number;
    private String type;
    private BigDecimal initialBalance;
    private Boolean status;
    private BigDecimal actualBalance;
    private UUID customerUuid;

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();
}
