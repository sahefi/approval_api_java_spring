package approval_api.approval_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.common.UserInfo;
import approval_api.approval_api.entity.Booking.BookStatus;
import approval_api.approval_api.model.BookingAdminFIlterRequest;
import approval_api.approval_api.model.BookingFilterRequest;
import approval_api.approval_api.model.CreateBookingRequest;
import approval_api.approval_api.model.CreateBookingResponse;
import approval_api.approval_api.model.GetBookingAdminResponse;
import approval_api.approval_api.model.GetBookingApporverResponse;
import approval_api.approval_api.model.RespondBookingRequest;
import approval_api.approval_api.model.UpdateUserRequest;
import approval_api.approval_api.model.UpdateUserResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.resolver.AuthRole;
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
    public WebResponse<CreateBookingResponse> register(
        @Token String token, 
        @AuthRole("Admin") String role,
        @RequestBody CreateBookingRequest request
        ){
        CreateBookingResponse createBookingResponse = bookingService.create(request);
        return WebResponse.<CreateBookingResponse>builder()
            .status("true")
            .message("Success")
            .data(createBookingResponse)
            .build();
    }

        @GetMapping(
            path = "/api/book/approver",
            produces = MediaType.APPLICATION_JSON_VALUE
            )
        public WebResponse<Page<GetBookingApporverResponse>> listBookingApprover(
            @Token String token,
            @AuthRole("Approver") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam (required = false) BookStatus status
            ) {
                BookingFilterRequest request = BookingFilterRequest.builder()
                    .status(status)
                    .build();

                UserInfo userInfo = authService.extractUserIdFromToken(token);             
                Page<GetBookingApporverResponse> getBookingApproverResponse = bookingService.getApprover( page,size,request,userInfo);
                return WebResponse.<Page<GetBookingApporverResponse>>
                        builder()
                        .status("true")
                        .message("Success")
                        .data(getBookingApproverResponse)
                        .build();
        }

        @PutMapping(
        path = "/api/book",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )

        public WebResponse respondRequestBooking(@Token String token, @RequestBody RespondBookingRequest request) {
            UserInfo userInfo = authService.extractUserIdFromToken(token);
            bookingService.respondRequestBooking(userInfo,request);
            return WebResponse
                    .builder()
                    .status("true")
                    .message("Success")
                    .data(null)
                    .build();
    }

    @GetMapping(
            path = "/api/book/admin",
            produces = MediaType.APPLICATION_JSON_VALUE
            )
        public WebResponse<Page<GetBookingAdminResponse>> listBookingAdmin(
            @Token String token,
            @AuthRole("Admin") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam (required = false) BookStatus status,
            @RequestParam (value = "approver",required = false) String approver
            ) {
                BookingAdminFIlterRequest request = BookingAdminFIlterRequest.builder()
                    .status(status)
                    .approver(approver)
                    .build();             
                Page<GetBookingAdminResponse> getBookingAdminResponse = bookingService.getApproverAdmin( page,size,request);
                return WebResponse.<Page<GetBookingAdminResponse>>
                        builder()
                        .status("true")
                        .message("Success")
                        .data(getBookingAdminResponse)
                        .build();
        }


}

