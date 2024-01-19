package approval_api.approval_api.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import approval_api.approval_api.entity.Booking.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBookingAdminResponse {

    private UUID id;

    private String driver;

    private String applicant;

   
    private VehicleResponse vehicle;


    private ApproverResponse approver;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate start_book;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate end_book;

    private BookStatus status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VehicleResponse{
        
        @JsonProperty("name")
        private String vehicle_name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ApproverResponse{
        
        @JsonProperty("name")
        private String approver_name;
    }
}
