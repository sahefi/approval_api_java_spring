package approval_api.approval_api.model;

import java.math.BigDecimal;

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

public class CreateVehicleRequest {
    
    @NotBlank
    private String name;
    

    @NotNull(message = "Fuel_Consumption Cannot Be Null")
    private BigDecimal fuel_consumption;

    @NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String service_schedule;
}
