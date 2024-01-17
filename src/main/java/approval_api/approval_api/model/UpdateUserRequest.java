package approval_api.approval_api.model;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateUserRequest {
    
    @JsonDeserialize
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private UUID role_id;

}
