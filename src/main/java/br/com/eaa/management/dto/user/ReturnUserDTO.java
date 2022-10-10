package br.com.eaa.management.dto.user;

import lombok.Data;

@Data
public class ReturnUserDTO {
    private Long id;
    private int age;
    private String name;
    private String username;
    private String email;
}
