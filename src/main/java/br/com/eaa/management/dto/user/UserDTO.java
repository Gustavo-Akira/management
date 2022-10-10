package br.com.eaa.management.dto.user;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
    private int age;
    private String email;
}
