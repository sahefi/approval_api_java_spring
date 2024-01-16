package approval_api.approval_api.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.Role;
import approval_api.approval_api.entity.User;
import approval_api.approval_api.model.GetUserResponse;
import approval_api.approval_api.model.RegisterUserRequest;
import approval_api.approval_api.model.RegisterUserResponse;
import approval_api.approval_api.repository.RoleRepository;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.security.BCrypt;


@Service

public class UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request){
        validationService.validate(request);

        if(userRepository.existsByUsername(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username Alredy Exist");

        }

        Role role = roleRepository.findById(request.getRole_id())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Role Not Found"));


        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt()));
        user.setRole(role);
        
        userRepository.save(user);
        
        return RegisterUserResponse.builder()
                .name(user.getName())
                .Username(user.getUsername())
                .build();

    }

    // @Transactional(readOnly = true)
    // public Page<GetUserResponse> get(int page,int size){
    //     Pageable pageable = PageRequest.of(page, size);
    //     Page<User> usersPage = userRepository.findAllByOrderByNameAsc(pageable)
    // }
}
