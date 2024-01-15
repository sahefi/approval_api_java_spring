package approval_api.approval_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import approval_api.approval_api.entity.Role;

@Repository

public interface RoleRepository extends JpaRepository< Role,UUID> {


}
