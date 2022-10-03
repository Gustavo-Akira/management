package br.com.eaa.management.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Location location;
    @OneToOne
    private User locator;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
}
