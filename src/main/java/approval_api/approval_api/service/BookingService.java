package approval_api.approval_api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.common.UserInfo;
import approval_api.approval_api.entity.Booking;
import approval_api.approval_api.entity.Role;
import approval_api.approval_api.entity.User;
import approval_api.approval_api.entity.Vehicle;
import approval_api.approval_api.entity.Booking.BookStatus;
import approval_api.approval_api.entity.Vehicle.VehicleStatus;
import approval_api.approval_api.model.BookingAdminFIlterRequest;
import approval_api.approval_api.model.BookingFilterRequest;
import approval_api.approval_api.model.CreateBookingRequest;
import approval_api.approval_api.model.CreateBookingResponse;
import approval_api.approval_api.model.GetBookingAdminResponse;
import approval_api.approval_api.model.GetBookingApporverResponse;
import approval_api.approval_api.model.RespondBookingRequest;
import approval_api.approval_api.repository.BookingRepository;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.repository.VehicleRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

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

        vehicle.setStatus(VehicleStatus.BOOKED);

        vehicleRepository.save(vehicle);
        bookingRepository.save(book);

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

    @Transactional(readOnly = true)
    public Page<GetBookingApporverResponse> getApprover( int page,int size,BookingFilterRequest request,UserInfo userInfo){

        Specification<Booking>specification = (root,query,builder) ->{
            List<Predicate>predicates = new ArrayList<>();
            if(request.getStatus() != null){
                predicates.add(builder.equal((root.get("status")),request.getStatus()));
            }

            predicates.add(builder.equal(root.get("approver").get("id"), userInfo.getUserId()));

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size,Sort.by("applicant").ascending());

        Page<Booking> bookingPage = bookingRepository.findAll(specification,pageable);
        
        List<GetBookingApporverResponse> getBookingApporverResponses = bookingPage.getContent().stream()
                .map(booking -> GetBookingApporverResponse.builder()
                    .id(booking.getId())
                    .applicant(booking.getApplicant())
                    .driver(booking.getDriver())
                    .vehicle((booking.getVehicle() != null) ? GetBookingApporverResponse.VehicleResponse.builder()
                        .vehicle_name(booking.getVehicle().getName())
                        .build():null) 
                    .start_book(booking.getStartBook())
                    .end_book(booking.getEndBook()) 
                    .status(booking.getStatus())                 
                    .build())
                .collect(Collectors.toList());

        return new PageImpl<>(getBookingApporverResponses, pageable,bookingPage.getTotalElements());
            

        
    }

    @Transactional
    public void respondRequestBooking (UserInfo userInfo,RespondBookingRequest request){

        Booking booking = bookingRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking ID Not Found"));

        booking.setStatus(request.getStatus());

        bookingRepository.save(booking);

    }

    @Transactional(readOnly = true)
    public Page<GetBookingAdminResponse> getApproverAdmin( int page,int size,BookingAdminFIlterRequest request){

        Specification<Booking>specification = (root,query,builder) ->{
            List<Predicate>predicates = new ArrayList<>();
            if(request.getStatus() != null){
                predicates.add(builder.equal((root.get("status")),request.getStatus()));
            }

            if(request.getApprover() != null){
                Join<Booking, User> userJoin = root.join("approver", JoinType.INNER);
                predicates.add(builder.like(builder.lower(userJoin.get("name")), "%" + request.getApprover().toLowerCase() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size,Sort.by("applicant").ascending());

        Page<Booking> bookingsPage = bookingRepository.findAll(specification,pageable);
        
        List<GetBookingAdminResponse> getBookingAdminResponses = bookingsPage.getContent().stream()
                .map(booking -> GetBookingAdminResponse.builder()
                    .id(booking.getId())
                    .applicant(booking.getApplicant())
                    .driver(booking.getDriver())
                    .vehicle((booking.getVehicle() != null) ? GetBookingAdminResponse.VehicleResponse.builder()
                        .vehicle_name(booking.getVehicle().getName())
                        .build():null) 
                    .approver((booking.getApprover() != null) ? GetBookingAdminResponse.ApproverResponse.builder()
                        .approver_name(booking.getApprover().getName())
                        .build():null)
                    .start_book(booking.getStartBook())
                    .end_book(booking.getEndBook()) 
                    .status(booking.getStatus())                 
                    .build())
                .collect(Collectors.toList());

        return new PageImpl<>(getBookingAdminResponses, pageable,bookingsPage.getTotalElements());
            

        
    }
}
