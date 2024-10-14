package bantads.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bantads.airline.model.MilesTransaction;

@Repository
public interface MilesTransactionRepository extends JpaRepository<MilesTransaction, UUID> {

}
