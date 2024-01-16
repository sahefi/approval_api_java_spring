package approval_api.approval_api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import approval_api.approval_api.entity.User;

@Repository

public interface UserRepository extends JpaRepository< User,UUID> {
    Page<User>findAllByOrderByNameAsc(Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByRoleId(UUID id);
}
