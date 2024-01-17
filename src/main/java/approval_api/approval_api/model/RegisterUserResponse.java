package approval_api.approval_api.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterUserResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("Username")
    private String Username;

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
