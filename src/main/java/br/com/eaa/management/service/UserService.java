package br.com.eaa.management.service;

import br.com.eaa.management.exceptions.NotFoundResourceException;
import br.com.eaa.management.model.User;
import br.com.eaa.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public boolean saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        user = repository.save(user);
        repository.addRole(user.getId());
        return repository.findById(user.getId()).isPresent();
    }

    public boolean removeUser(Long id){
        User user = getById(id);
        user.setActive(false);
        repository.save(user);
        return !repository.findById(id).get().isEnabled();
    }

    public User getById(Long id){
        User user = repository.findById(id).orElseThrow(()->new NotFoundResourceException("User not found"));
        if(!user.isActive()){
            throw new NotFoundResourceException("User not Found");
        }
        return user;
    }

    public Page<User> getAll(int page, int size){
        return new PageImpl<>( repository.findAll(Pageable.ofSize(size).withPage(page)).stream().filter(user -> user.isActive()).collect(Collectors.toList()));
    }

    public User updateUser(User user){
        User oldUser = getById(user.getId());
        if(user.getName() != null && !user.getName().isEmpty()){
            oldUser.setName(user.getName());
        }
        if(user.getAge() > 18 && user.getAge() != 0){
            oldUser.setAge(user.getAge());
        }
        if(user.getUsername() != null && !user.getUsername().isEmpty()){
            oldUser.setUsername(user.getUsername());
        }
        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            oldUser.setPassword(user.getPassword());
        }
        return repository.save(oldUser);
    }
}
