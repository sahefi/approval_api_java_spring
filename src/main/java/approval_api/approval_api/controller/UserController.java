package approval_api.approval_api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.model.DeleteUserRequest;
import approval_api.approval_api.model.GetUserResponse;
import approval_api.approval_api.model.RegisterUserRequest;
import approval_api.approval_api.model.RegisterUserResponse;
import approval_api.approval_api.model.UpdateUserRequest;
import approval_api.approval_api.model.UpdateUserResponse;
import approval_api.approval_api.model.UserSearchRequest;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
        path =  "/api/register", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public WebResponse<RegisterUserResponse> register(@RequestBody RegisterUserRequest request){
        RegisterUserResponse registerUserResponse = userService.register(request);
        return WebResponse.<RegisterUserResponse>builder().status("true").message("Success").data(registerUserResponse).build();
    }

    @GetMapping(
        path = "/api/user",
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public WebResponse<Page<GetUserResponse>> ListUser(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam (value = "name",required = false) String name,
        @RequestParam (value = "role_name",required = false) String roleName
        ) {
        UserSearchRequest request = UserSearchRequest.builder()
            .name(name)
            .role_name(roleName)
            .build();

            Page<GetUserResponse> getUserResponse = userService.get(page,size,request);
            
            return WebResponse.<Page<GetUserResponse>>
                    builder()
                    .status("true")
                    .message("Success")
                    .data(getUserResponse)
                    .build();
    }

     @PutMapping(
        path = "/api/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse updateUser(@RequestBody UpdateUserRequest request) {
        UpdateUserResponse updateUserResponse = userService.update(request);
        return WebResponse.<UpdateUserResponse>
                builder()
                .status("true")
                .message("Success")
                .data(updateUserResponse)
                .build();
    }

     @DeleteMapping(
        path = "/api/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse deleteUser(@RequestBody DeleteUserRequest request) {
        userService.delete(request);
        return WebResponse
                .builder()
                .status("true")
                .message("Success")
                .data(null)
                .build();
    }
    
}
