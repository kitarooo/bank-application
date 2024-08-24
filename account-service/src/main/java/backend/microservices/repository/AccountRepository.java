package backend.microservices.repository;

import backend.microservices.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //@Query(name = "SELECT * FROM accounts WHERE user_id",nativeQuery = true)
    Optional<Account> findByUserId(Long userId);
    List<Account> findAllByUserId(Long userId);
    Optional<Account> findAByAccountNumber(String accountNumber);
    @Query(value = "SELECT a.balance FROM accounts a WHERE a.account_number= :accountNumber", nativeQuery = true)
    Long findBalanceByAccountNumber(@Param("accountNumber") String accountNumber);
    @Query(value = "SELECT *.a FROM accounts a WHERE a.accoun_number= :accountNumber", nativeQuery = true)
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);
}
