package approval_api.approval_api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import approval_api.approval_api.model.CreateBookingResponse.VehicleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBookingApporverResponse {
    
    private String applicant;

    private String driver;

    private VehicleResponse vehicle;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate start_book;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate end_book;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VehicleResponse{
        
        @JsonProperty("name")
        private String vehicle_name;
    }


}
