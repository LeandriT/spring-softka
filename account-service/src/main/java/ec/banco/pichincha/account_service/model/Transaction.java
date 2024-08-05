package ec.banco.pichincha.account_service.model;

import ec.banco.pichincha.account_service.model.abstracts.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
@SQLDelete(sql = "UPDATE transactions SET is_deleted = true, deleted_at=now() WHERE uuid=?")
@Where(clause = "is_deleted is false")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseModel {

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String transactionType;

    @Column(nullable = false)
    private BigDecimal amount; //valor

    @Column(nullable = false)
    private BigDecimal balance; //saldo

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
