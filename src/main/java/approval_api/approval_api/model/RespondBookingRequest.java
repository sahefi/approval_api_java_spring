package approval_api.approval_api.model;

import java.util.UUID;

import approval_api.approval_api.entity.Booking.BookStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondBookingRequest {
    @NotNull(message = "ID Cannot Be Null")
    private UUID id;

    @NotNull(message = "Status Canoot Be Null")
    private BookStatus status;



}
