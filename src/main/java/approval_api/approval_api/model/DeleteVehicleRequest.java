package approval_api.approval_api.model;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DeleteVehicleRequest {

    @NotNull(message = "ID Canoot Be Null")
    private UUID id;
    
}
