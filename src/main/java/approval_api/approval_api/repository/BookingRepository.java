package approval_api.approval_api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import approval_api.approval_api.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking,UUID>,JpaSpecificationExecutor<Booking> {
    Page<Booking> findAllByApproverId(Specification<Booking> specification,UUID userId,Pageable pageable);
}
