package approval_api.approval_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.model.CreateBookingRequest;
import approval_api.approval_api.model.CreateBookingResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.service.BookingService;

@RestController
public class BookingController {
    @Autowired
    private BookingService bookingService;

    
     @PostMapping(
        path =  "/api/book", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public WebResponse<CreateBookingResponse> register(@RequestBody CreateBookingRequest request){
        CreateBookingResponse createBookingResponse = bookingService.create(request);
        return WebResponse.<CreateBookingResponse>builder()
            .status("true")
            .message("Success")
            .data(createBookingResponse)
            .build();
    }

}
