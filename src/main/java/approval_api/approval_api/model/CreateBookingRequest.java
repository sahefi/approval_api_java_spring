package approval_api.approval_api.model;

import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateBookingRequest {

    @NotBlank
    private String driver;
    
    @NotBlank
    private String applicant;

    @NotNull(message = "Vehicle ID Cannot Be NUll")
    private UUID vehicle_id;

    @NotNull(message = "Approver ID Cannot Be Null")
    private UUID approver_id;

    @NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String start_book;

    @NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String end_book;


}
