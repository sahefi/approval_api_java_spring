package approval_api.approval_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import approval_api.approval_api.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle,UUID> {
    
}
