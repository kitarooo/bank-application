package backend.microservices.accountservice.repository;

import backend.microservices.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //@Query(name = "SELECT * FROM accounts WHERE user_id",nativeQuery = true)
    Optional<Account> findByUserId(Long userId);
    List<Account> findAllByUserId(Long userId);
}
