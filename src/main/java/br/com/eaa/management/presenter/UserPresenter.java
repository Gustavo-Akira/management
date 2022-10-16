package br.com.eaa.management.presenter;

import br.com.eaa.management.dto.status.CreateStatusDTO;
import br.com.eaa.management.dto.status.DeletedStatus;
import br.com.eaa.management.dto.user.InsertUserDTO;
import br.com.eaa.management.dto.user.ReturnUserDTO;
import br.com.eaa.management.dto.user.UserDTO;
import br.com.eaa.management.exceptions.SecurityException;
import br.com.eaa.management.model.User;
import br.com.eaa.management.security.SecurityUtility;
import br.com.eaa.management.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class UserPresenter {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecurityUtility utility;

    @GetMapping("users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<ReturnUserDTO> getUsers(){
        return service.getAll(0,5).map(x->modelMapper.map(x,ReturnUserDTO.class));
    }

    @GetMapping("user/{id}")
    public ReturnUserDTO getUser(@PathVariable Long id, Authentication authentication){
        if(!utility.isAdmin(authentication)){
            if(!utility.isSameUser(id,authentication)){
                throw new SecurityException("User cannot see another user details");
            }
        }
        return modelMapper.map(service.getById(id), ReturnUserDTO.class);
    }

    @GetMapping("users/page/{page}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<ReturnUserDTO> getUsers(@PathVariable int page){
        return service.getAll(page,5).map(x->modelMapper.map(x,ReturnUserDTO.class));
    }

    @PostMapping("user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CreateStatusDTO saveUser(@RequestBody InsertUserDTO dto){
        return new CreateStatusDTO(service.saveUser(modelMapper.map(dto,User.class)));
    }

    @PutMapping("user/{id}")
    public ReturnUserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO dto, Authentication authentication){
        if(!utility.isAdmin(authentication)){
            if(!utility.isSameUser(id,authentication)){
                throw new SecurityException("User cannot update another user");
            }
        }
        dto.setId(id);
        return modelMapper.map(service.updateUser(modelMapper.map(dto,User.class)),ReturnUserDTO.class);
    }

    @DeleteMapping("user/{id}")
    public DeletedStatus deleteUser(@PathVariable Long id, Authentication authentication){
        if(!utility.isAdmin(authentication)){
            if(!utility.isSameUser(id,authentication)){
                throw new SecurityException("User cannot delete another user");
            }
        }
        return new DeletedStatus(service.removeUser(id));
    }
}
