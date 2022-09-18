package br.com.eaa.management.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "LocationxCaracteristic",joinColumns = @JoinColumn(name = "location_id"), inverseJoinColumns = @JoinColumn(name = "caracteristic_id"))
    private List<Characteristic> characteristics = new ArrayList<>();

}
