package approval_api.approval_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.common.UserInfo;
import approval_api.approval_api.model.CreateBookingRequest;
import approval_api.approval_api.model.CreateBookingResponse;
import approval_api.approval_api.model.GetBookingApporverResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.resolver.Token;
import approval_api.approval_api.service.AuthService;
import approval_api.approval_api.service.BookingService;

@RestController
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private AuthService authService;

    
     @PostMapping(
        path =  "/api/book", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE

        )
    public WebResponse<CreateBookingResponse> register(@Token @RequestBody CreateBookingRequest request){
        CreateBookingResponse createBookingResponse = bookingService.create(request);
        return WebResponse.<CreateBookingResponse>builder()
            .status("true")
            .message("Success")
            .data(createBookingResponse)
            .build();
    }

        @GetMapping(
            path = "/api/book",
            produces = MediaType.APPLICATION_JSON_VALUE
            )
        public WebResponse<Page<GetBookingApporverResponse>> ListBookingApprover(
            @Token String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
                UserInfo userInfo = authService.extractUserIdFromToken(token);             
                Page<GetBookingApporverResponse> getBookingApproverResponse = bookingService.getApprover( userInfo,page,size);
                return WebResponse.<Page<GetBookingApporverResponse>>
                        builder()
                        .status("true")
                        .message("Success")
                        .data(getBookingApproverResponse)
                        .build();
        }

}

