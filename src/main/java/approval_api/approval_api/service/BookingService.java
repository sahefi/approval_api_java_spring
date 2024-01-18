package approval_api.approval_api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.Booking;
import approval_api.approval_api.entity.Role;
import approval_api.approval_api.entity.User;
import approval_api.approval_api.entity.Vehicle;
import approval_api.approval_api.entity.Booking.BookStatus;
import approval_api.approval_api.entity.Vehicle.VehicleStatus;
import approval_api.approval_api.model.CreateBookingRequest;
import approval_api.approval_api.model.CreateBookingResponse;
import approval_api.approval_api.repository.BookingRepository;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.repository.VehicleRepository;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional()
    public CreateBookingResponse create(CreateBookingRequest request){
        validationService.validate(request);
        //cek id approver
        User approver = userRepository.findById(request.getApprover_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Approver ID Not Found"));
        //cek id vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVehicle_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle ID Not Found"));
        
        //cek approver role = Approver
        Role approverRole = approver.getRole();
        if(!approverRole.getName().equals("Approver")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Approver ID Is Invalid");
        }
        //cek vehicle status = AVAILABLE
        if(!VehicleStatus.AVAILABLE.equals(vehicle.getStatus())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Vehicle Is Not Available");
        }

        
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter currentFormatted = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String startBook = request.getStart_book();
        String endBook = request.getEnd_book();

        //format string agar dapat di validasi
        LocalDate parsedStart = LocalDate.parse(startBook,currentFormatted);
        LocalDate parsedEnd = LocalDate.parse(endBook,currentFormatted);

        //validasi !start < current
        if(parsedStart.isBefore(currentDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Start Date");
        }
        //validasi !end < start
        if(parsedEnd.isBefore(parsedStart)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid End Date");
        }

        Booking book = new Booking();
        book.setDriver(request.getDriver());
        book.setApplicant(request.getApplicant());
        book.setVehicle(vehicle);
        book.setApprover(approver);
        book.setStartBook(parsedStart);
        book.setEndBook(parsedEnd);
        book.setStatus(BookStatus.PENDING);

        return CreateBookingResponse.builder()
            .driver(book.getDriver())
            .applicant(book.getApplicant())
            .start_book(book.getStartBook())
            .end_book(book.getEndBook())
            .status(book.getStatus())
            .vehicle((book.getVehicle() != null) ? CreateBookingResponse.VehicleResponse.builder()
                .vehicle_name(book.getVehicle().getName())
                .build():null)
            .approver((book.getApprover() != null) ? CreateBookingResponse.ApproverResponse.builder()
                .approver_name(book.getApprover().getName())
                .build():null)   
            .build();

    }
}
