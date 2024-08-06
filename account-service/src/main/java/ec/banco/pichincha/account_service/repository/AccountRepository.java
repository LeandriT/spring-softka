package ec.banco.pichincha.account_service.repository;

import ec.banco.pichincha.account_service.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query(nativeQuery = true,
            value = "select a.* from transactions t join accounts a on t.account_id = a.uuid where a.customer_uuid = :customerUuid \n" +
                    "and t.date between TO_TIMESTAMP(:startDate, 'YYYY-MM-DD HH24:MI:SS') and TO_TIMESTAMP(:endDate, 'YYYY-MM-DD HH24:MI:SS')")
    Page<Account> findByCustomerUuidAndStartDateAndEndDate(
            Pageable pageable,
            @Param("customerUuid") UUID customerUuid,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}