package br.com.eaa.management.security;

import br.com.eaa.management.model.User;
import br.com.eaa.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtility {

    @Autowired
    private UserRepository repository;

    public boolean isAdmin(Authentication authentication){
        return authentication.getAuthorities().stream().filter(x->x.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent();
    }

    public boolean isSameUser(Long id, Authentication authentication){
        return repository.findUserByUsername(authentication.getPrincipal().toString()).getId().equals(id);
    }
}
