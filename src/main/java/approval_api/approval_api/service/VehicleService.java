package approval_api.approval_api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.Vehicle;
import approval_api.approval_api.entity.Vehicle.VehicleStatus;
import approval_api.approval_api.model.CreateVehicleRequest;
import approval_api.approval_api.model.CreateVehicleResponse;
import approval_api.approval_api.model.DeleteVehicleRequest;
import approval_api.approval_api.model.GetVehicleResponse;
import approval_api.approval_api.model.UpdateVehicleRequest;
import approval_api.approval_api.model.UpdateVehicleResponse;
import approval_api.approval_api.repository.VehicleRepository;

@Service

public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public CreateVehicleResponse create(CreateVehicleRequest request){
        validationService.validate(request);

        LocalDate currenDate = LocalDate.now();
        DateTimeFormatter currentFormatted = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String requestDate = request.getService_schedule();

        LocalDate parsedRequestDate = LocalDate.parse(requestDate, currentFormatted);

        if( parsedRequestDate.isBefore(currenDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Date");
        }
        
        Vehicle vehicle = new Vehicle();
        vehicle.setName(request.getName());
        vehicle.setFuelConsumption(request.getFuel_consumption());
        vehicle.setServiceSchedule(parsedRequestDate);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        
        vehicleRepository.save(vehicle);

        return CreateVehicleResponse.builder()
                .name(vehicle.getName())
                .fuel_consumption(vehicle.getFuelConsumption())
                .service_schedule(vehicle.getServiceSchedule())
                .status(vehicle.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<GetVehicleResponse> get (int page,int size){

        Pageable pageable = PageRequest.of(page, size,Sort.by("name").ascending());
        Page<Vehicle> vehiclePage = vehicleRepository.findAll(pageable);
        
        List<GetVehicleResponse> getVehicleResponses = vehiclePage.getContent().stream()
                .map(vehicle -> GetVehicleResponse.builder()
                        .id(vehicle.getId())
                        .name(vehicle.getName())
                        .fuel_consumption(vehicle.getFuelConsumption())
                        .service_schedule(vehicle.getServiceSchedule())
                        .status(vehicle.getStatus())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(getVehicleResponses, pageable, vehiclePage.getTotalElements());

    }

    @Transactional
    public UpdateVehicleResponse update(UpdateVehicleRequest request){
        validationService.validate(request);

        LocalDate currenDate = LocalDate.now();
        DateTimeFormatter currentFormatted = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String requestDate = request.getService_schedule();

        LocalDate parsedRequestDate = LocalDate.parse(requestDate, currentFormatted);

        if( parsedRequestDate.isBefore(currenDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Date");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle ID Not Found"));

        vehicle.setName(request.getName());
        vehicle.setFuelConsumption(request.getFuel_consumption());
        vehicle.setServiceSchedule(parsedRequestDate);
        vehicle.setStatus(request.getStatus());

        vehicleRepository.save(vehicle);

        return new UpdateVehicleResponse().builder()
                .name(vehicle.getName())
                .fuel_consumption(vehicle.getFuelConsumption())
                .service_schedule(vehicle.getServiceSchedule())
                .status(vehicle.getStatus())
                .build();
    }

    @Transactional
    public void delete(DeleteVehicleRequest request){

        Vehicle vehicle = vehicleRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle ID Not Found"));

                vehicleRepository.delete(vehicle);
    }

}
