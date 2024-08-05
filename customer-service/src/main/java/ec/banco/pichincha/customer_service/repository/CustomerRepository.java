package ec.banco.pichincha.customer_service.repository;

import ec.banco.pichincha.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByPersonIdentification(String identification);
}
