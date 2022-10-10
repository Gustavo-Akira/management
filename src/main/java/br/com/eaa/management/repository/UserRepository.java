package br.com.eaa.management.repository;

import br.com.eaa.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    @Transactional
    @Modifying
    @Query(value = "insert into usuario_role (usuario_id, role_id) values (?1, (select id from role where role_name = 'ROLE_USER'));", nativeQuery = true)
    void addRole(Long idUsuario);
}
