package approval_api.approval_api.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.Role;
import approval_api.approval_api.entity.User;
import approval_api.approval_api.model.RegisterUserRequest;
import approval_api.approval_api.model.RegisterUserResponse;
import approval_api.approval_api.repository.RoleRepository;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.security.BCrypt;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
@Transactional
public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    public RegisterUserResponse register(RegisterUserRequest request){
        Set<ConstraintViolation<RegisterUserRequest>>constraintViolations = validator.validate(request);
        if(constraintViolations.size() != 0 ){
            throw new ConstraintViolationException(constraintViolations);
        }

        if(userRepository.existsByUsername(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username Alredy Exist");

        }

        Role role = roleRepository.findById(request.getRole_id())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Role Not Found"));


        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
        
        userRepository.save(user);
        
        return RegisterUserResponse.builder()
                .name(user.getName())
                .Username(user.getUsername())
                .build();

    }
}
