package approval_api.approval_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.model.CreateBookingRequest;
import approval_api.approval_api.model.CreateBookingResponse;
import approval_api.approval_api.model.LoginUserRequest;
import approval_api.approval_api.model.LoginUserResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
        path =  "/api/auth", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public WebResponse<LoginUserResponse> register(@RequestBody LoginUserRequest request){
        LoginUserResponse loginUserResponse = authService.login(request);
        return WebResponse.<LoginUserResponse>builder()
            .status("true")
            .message("Success")
            .data(loginUserResponse)
            .build();
    }
}
