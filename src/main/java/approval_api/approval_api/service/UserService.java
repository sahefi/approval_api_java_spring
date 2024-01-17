package approval_api.approval_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.Role;
import approval_api.approval_api.entity.User;
import approval_api.approval_api.model.DeleteUserRequest;
import approval_api.approval_api.model.GetUserResponse;
import approval_api.approval_api.model.RegisterUserRequest;
import approval_api.approval_api.model.RegisterUserResponse;
import approval_api.approval_api.model.UpdateUserRequest;
import approval_api.approval_api.model.UpdateUserResponse;
import approval_api.approval_api.model.UserSearchRequest;
import approval_api.approval_api.repository.RoleRepository;
import approval_api.approval_api.repository.UserRepository;
import approval_api.approval_api.security.BCrypt;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;


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
                .role(RegisterUserResponse.RoleResponse
                    .builder()
                    .role_name(role.getName())
                    .build())
                .build();

    }

    @Transactional(readOnly = true)
    public Page<GetUserResponse> get(int page,int size,UserSearchRequest request){
        Specification<User>specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(request.getName())){
                predicates.add(builder.like(builder.lower(root.get("name")),"%"+request.getName()+"%"));
            
            }
            if(StringUtils.hasText(request.getRole_name())){
                Join<User, Role> roleJoin = root.join("role", JoinType.INNER);
            predicates.add(builder.like(builder.lower(roleJoin.get("name")), "%" + request.getRole_name() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        Pageable pageable = PageRequest.of(page, size,Sort.by("name").ascending());
        Page<User> usersPage = userRepository.findAll(specification,pageable);

        List<GetUserResponse> getUserResponses = usersPage.getContent().stream()
                .map(user -> GetUserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .username(user.getUsername())
                        .role((user.getRole() != null) ? GetUserResponse.RoleResponse.builder()
                                .role_name(user.getRole().getName())
                                .build() : null)
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(getUserResponses,pageable,usersPage.getTotalElements());
    }

    @Transactional
    public UpdateUserResponse update(UpdateUserRequest request){
        validationService.validate(request);

        if(request.getId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User ID Cannot Be Null");
        }

        if(request.getRole_id() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role ID Cannot Be Null");
        }

        User user = userRepository.findById(request.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User ID Not Found"));

        Role role = roleRepository.findById(request.getRole_id())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Role ID Not Found"));
    
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        
        user.setRole(role);
        

        return UpdateUserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .role(UpdateUserResponse.RoleResponse
                    .builder()
                    .role_name(role.getName())
                    .build())
                .build();
            }

    @Transactional
    public void delete(DeleteUserRequest request){
    

        User user = userRepository.findById(request.getId())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User ID Not Found"));

            userRepository.delete(user);
    }
}
