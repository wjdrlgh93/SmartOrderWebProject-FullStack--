package org.spring.backendspring.member.repository;

import java.util.Optional;
import org.spring.backendspring.member.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Optional<RefreshEntity> findByUserEmail(String userEmail);

    Optional<RefreshEntity> findByRefresh(String refreshToken);
}
