package br.com.eaa.management.dto.user;

import lombok.Data;

@Data
public class InsertUserDTO {
    private String name;
    private int age;
    private String username;
    private String password;
}
