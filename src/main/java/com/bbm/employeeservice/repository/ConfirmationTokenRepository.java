package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);
    @Query("""
                select t from ConfirmationToken t inner join User u on t.user.id = u.id
                where u.id = :userId and (t.expired = false or t.revoked = false)
            """)
    List<ConfirmationToken> findAllValidTokenByUser(Long userId);
}
