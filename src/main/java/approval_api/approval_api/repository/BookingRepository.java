package approval_api.approval_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import approval_api.approval_api.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking,UUID> {
    Page<Booking> findAllByApproverId(UUID userId,Pageable pageable);
}
