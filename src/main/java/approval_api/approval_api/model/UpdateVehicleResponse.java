package approval_api.approval_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import approval_api.approval_api.entity.Vehicle.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateVehicleResponse {

    private String name;

    private BigDecimal fuel_consumption;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate service_schedule;

    private VehicleStatus status;
}
