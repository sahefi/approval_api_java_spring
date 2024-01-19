package approval_api.approval_api.model;

import approval_api.approval_api.entity.Booking.BookStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingFilterRequest {

    @Enumerated(EnumType.STRING)
    private BookStatus status;
}
