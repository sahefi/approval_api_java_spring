package approval_api.approval_api.model;

import approval_api.approval_api.entity.Booking.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAdminFIlterRequest {
    private String approver;

    private BookStatus status;
}
