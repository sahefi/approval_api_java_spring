package approval_api.approval_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.model.CreateRoleRequest;
import approval_api.approval_api.model.CreateRoleResponse;
import approval_api.approval_api.model.DeleteRoleRequest;
import approval_api.approval_api.model.GetRoleResponse;
import approval_api.approval_api.model.UpdateRoleRequest;
import approval_api.approval_api.model.UpdateRoleResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.service.RoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping(
        path = "/api/role",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse createRole(@RequestBody CreateRoleRequest request) {
        CreateRoleResponse createRoleResponse = roleService.create(request);
        return WebResponse.<CreateRoleResponse>
                builder()
                .status("true")
                .message("Success")
                .data(createRoleResponse)
                .build();
    }

    @GetMapping(
        path = "/api/role",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Page<GetRoleResponse>> listRole(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<GetRoleResponse> getRoleResponse = roleService.get(page,size);
        return WebResponse.<Page<GetRoleResponse>>
                builder()
                .status("true")
                .message("Success")
                .data(getRoleResponse)
                .build();

    }

    @PutMapping(
        path = "/api/role",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse updateRole(@RequestBody UpdateRoleRequest request) {
        UpdateRoleResponse updateRoleResponse = roleService.update(request);
        return WebResponse.<UpdateRoleResponse>
                builder()
                .status("true")
                .message("Success")
                .data(updateRoleResponse)
                .build();
    }

    @DeleteMapping(
        path = "/api/role",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse DeleteRole(@RequestBody DeleteRoleRequest request) {
        roleService.delete(request);
        return WebResponse
                .builder()
                .status("true")
                .message("Success")
                .data(null)
                .build();
    }
    
    
    
}
