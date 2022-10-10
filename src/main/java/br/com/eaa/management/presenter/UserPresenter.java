package br.com.eaa.management.presenter;

import br.com.eaa.management.dto.user.InsertUserDTO;
import br.com.eaa.management.dto.user.ReturnUserDTO;
import br.com.eaa.management.dto.user.UserDTO;
import br.com.eaa.management.model.User;
import br.com.eaa.management.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserPresenter {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("users")
    public Page<ReturnUserDTO> getUsers(){
        return service.getAll(0,5).map(x->modelMapper.map(x,ReturnUserDTO.class));
    }

    @GetMapping("user/{id}")
    public ReturnUserDTO getUser(@PathVariable Long id){
        return modelMapper.map(service.getById(id), ReturnUserDTO.class);
    }

    @GetMapping("users/page/{page}")
    public Page<ReturnUserDTO> getUsers(@PathVariable int page){
        return service.getAll(page,5).map(x->modelMapper.map(x,ReturnUserDTO.class));
    }

    @PostMapping("user")
    public boolean saveUser(@RequestBody InsertUserDTO dto){
        return service.saveUser(modelMapper.map(dto,User.class));
    }

    @PutMapping("user/{id}")
    public ReturnUserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO dto){
        dto.setId(id);
        return modelMapper.map(service.updateUser(modelMapper.map(dto,User.class)),ReturnUserDTO.class);
    }

    @DeleteMapping("user/{id}")
    public boolean deleteUser(@PathVariable Long id){
        return service.removeUser(id);
    }
}
