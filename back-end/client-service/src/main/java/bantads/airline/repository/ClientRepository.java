package bantads.airline.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bantads.airline.model.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByCpfAndEmail(String cpf, String email);

    @Query(value = "SELECT * FROM client_table c WHERE c.cpf = ?1", nativeQuery = true)
    Client getClientByCpf(String cpf);

    // native query with native param
    @Query(value = "SELECT * FROM client_table c WHERE c.email = :email", nativeQuery = true)
    Client getClientByEmail(@Param("email") String email);

    // query using JPQL
    @Query("select c.miles from Client c where c.userId = ?1")
    Integer getMilesBalanceByClientUserId(String userId);

}
