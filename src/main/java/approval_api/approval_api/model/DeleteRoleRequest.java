package approval_api.approval_api.model;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DeleteRoleRequest {

    @JsonDeserialize
    private UUID id;
}
