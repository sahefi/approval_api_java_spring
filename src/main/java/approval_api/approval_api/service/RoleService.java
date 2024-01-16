package approval_api.approval_api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import approval_api.approval_api.entity.Role;
import approval_api.approval_api.model.CreateRoleRequest;
import approval_api.approval_api.model.CreateRoleResponse;
import approval_api.approval_api.model.DeleteRoleRequest;
import approval_api.approval_api.model.GetRoleResponse;
import approval_api.approval_api.model.UpdateRoleRequest;
import approval_api.approval_api.model.UpdateRoleResponse;
import approval_api.approval_api.repository.RoleRepository;
import approval_api.approval_api.repository.UserRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional()
    public CreateRoleResponse create(CreateRoleRequest request){
        validationService.validate(request);

        Role role = new Role();
        role.setName(request.getName());

        roleRepository.save(role);

        return CreateRoleResponse.builder()
                .name(role.getName())
                .build();



    }

    @Transactional(readOnly = true)
    public Page<GetRoleResponse> get(int page, int size){
        
        Pageable pageable = PageRequest.of(page,size);
        Page<Role> rolespPage = roleRepository.findAllByOrderByNameAsc(pageable);

        return rolespPage.map(role -> new GetRoleResponse(
                    role.getId(),
                    role.getName()
                        )
                    );
    }

    @Transactional
    public UpdateRoleResponse update(UpdateRoleRequest request){
        validationService.validate(request);

        if(request.getId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role ID Cannot Be Null");
        }

        Role role = roleRepository.findById(request.getId())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Role ID Not Found"));

        role.setName(request.getName());

        return UpdateRoleResponse.builder()
                .name(role.getName())
                .build();
        
    }

    @Transactional
    public void delete(DeleteRoleRequest request){
        validationService.validate(request);

        Role role = roleRepository.findById(request.getId())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Role ID Not Found"));
        
        if(userRepository.existsByRoleId(request.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Role cannot be deleted because there are users associated with it");
        }
        
        roleRepository.delete(role);
        
    }
}
