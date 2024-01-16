package approval_api.approval_api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.model.RegisterUserRequest;
import approval_api.approval_api.model.RegisterUserResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.service.UserService;

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
}
