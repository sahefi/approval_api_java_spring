package approval_api.approval_api.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import approval_api.approval_api.entity.Vehicle;
import approval_api.approval_api.entity.Vehicle.VehicleStatus;
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
public class UpdateVehicleRequest {

    @NotNull(message = "ID Cannot Be Null")
    private UUID id;

    @NotBlank
    private String name;
    

    @NotNull(message = "Fuel_Consumption Cannot Be Null")
    private BigDecimal fuel_consumption;

    @NotBlank
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String service_schedule;

    @NotNull(message = "Status Cannot Be Null")
    private VehicleStatus status;
}
