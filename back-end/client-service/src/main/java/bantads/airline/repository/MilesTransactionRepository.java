package bantads.airline.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bantads.airline.model.MilesTransaction;

@Repository
public interface MilesTransactionRepository extends JpaRepository<MilesTransaction, UUID> {

    @Query(value = "SELECT * FROM transaction_table t WHERE t.client_id = ?1 AND t.description = ?2", nativeQuery = true)
    List<MilesTransaction> getClientPurchases(UUID clientId, String status);

}
