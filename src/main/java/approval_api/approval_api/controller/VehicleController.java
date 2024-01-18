package approval_api.approval_api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import approval_api.approval_api.model.CreateVehicleRequest;
import approval_api.approval_api.model.CreateVehicleResponse;
import approval_api.approval_api.model.DeleteVehicleRequest;
import approval_api.approval_api.model.GetVehicleResponse;
import approval_api.approval_api.model.UpdateVehicleRequest;
import approval_api.approval_api.model.UpdateVehicleResponse;
import approval_api.approval_api.model.WebResponse;
import approval_api.approval_api.resolver.Token;
import approval_api.approval_api.service.VehicleService;

@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;


     @PostMapping(
        path =  "/api/vehicle", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public WebResponse<CreateVehicleResponse> register(@Token String token,@RequestBody CreateVehicleRequest request ){
        CreateVehicleResponse createVehicleResponse = vehicleService.create(request);
        return WebResponse.<CreateVehicleResponse>builder().status("true").message("Success").data(createVehicleResponse).build();
    }

    @GetMapping(
        path = "/api/vehicle",
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public WebResponse<Page<GetVehicleResponse>> ListUser(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
        ) {
        Page<GetVehicleResponse> getVehicleResponse = vehicleService.get(page,size);
            
            return WebResponse.<Page<GetVehicleResponse>>
                    builder()
                    .status("true")
                    .message("Success")
                    .data(getVehicleResponse)
                    .build();
    }


    @PutMapping(
        path = "/api/vehicle",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse updateVehicle(@RequestBody UpdateVehicleRequest request) {
        UpdateVehicleResponse updateVehicleResponse = vehicleService.update(request);
        return WebResponse.<UpdateVehicleResponse>
                builder()
                .status("true")
                .message("Success")
                .data(updateVehicleResponse)
                .build();
    }

    @DeleteMapping(
        path = "/api/vehicle",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse deleteUser(@RequestBody DeleteVehicleRequest request) {
        vehicleService.delete(request);
        return WebResponse
                .builder()
                .status("true")
                .message("Success")
                .data(null)
                .build();
    }

}
