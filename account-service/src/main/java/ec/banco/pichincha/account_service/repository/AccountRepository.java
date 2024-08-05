package ec.banco.pichincha.account_service.repository;

import ec.banco.pichincha.account_service.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query(nativeQuery = true,
            value = "select a.* from transactions t join accounts a on t.account_id = a.uuid where a.customer_uuid ='446a6666-8d0a-42d6-83d1-a1b4a9ee2e1f'\n" +
                    "and t.date between '2024-08-02 00:00:00.000' and '2024-08-02 23:59:00.000'")
    Page<Account> findByCustomerUuidAndStartDateAndEndDate(
            Pageable pageable,
            @Param("customerUuid") UUID customerUuid,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}