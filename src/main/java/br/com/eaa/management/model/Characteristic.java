package br.com.eaa.management.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="Caracteristic")
@Data
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
