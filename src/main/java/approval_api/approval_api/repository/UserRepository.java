package approval_api.approval_api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import approval_api.approval_api.entity.User;
import java.util.List;


@Repository

public interface UserRepository extends JpaRepository< User,UUID>,JpaSpecificationExecutor<User> {
    Optional<User>findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRoleId(UUID id);
}
