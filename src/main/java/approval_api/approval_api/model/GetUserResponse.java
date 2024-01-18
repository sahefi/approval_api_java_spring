package approval_api.approval_api.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserResponse {
    private UUID id;

    private String name;

    private String username;

    private RoleResponse role;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoleResponse{
        
        @JsonProperty("name")
        private String role_name;
    }
}
