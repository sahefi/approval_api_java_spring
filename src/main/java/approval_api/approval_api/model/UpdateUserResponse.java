package approval_api.approval_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UpdateUserResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
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
