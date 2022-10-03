package br.com.eaa.management.service;

import br.com.eaa.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        br.com.eaa.management.model.User usuario = repository.findUserByUsername(username);
        if(usuario == null) {
            throw new UsernameNotFoundException("Usuario não encontrado");
        }
        return new User(usuario.getUsername(),usuario.getPassword(),usuario.getAuthorities());
    }
}
