package br.com.eaa.management.model;

import br.com.eaa.management.dto.location.ReturnLocationDTO;
import br.com.eaa.management.dto.reservation.ReturnReservationDTO;
import br.com.eaa.management.dto.user.ReturnUserDTO;
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
    @ManyToOne
    private User locator;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isConfirmed = false;

    public ReturnReservationDTO toDTO(){
        ReturnReservationDTO dto = new ReturnReservationDTO();
        ReturnLocationDTO locationDTO = new ReturnLocationDTO();
        locationDTO.setName(location.getName());
        locationDTO.setId(location.getId());
        dto.setDto(locationDTO);
        ReturnUserDTO userDTO = new ReturnUserDTO();
        userDTO.setId(locator.getId());
        userDTO.setName(locator.getName());
        dto.setUserDTO(userDTO);
        dto.setId(id);
        dto.setEndTime(endTime);
        dto.setStartTime(startTime);
        dto.setIsConfirmed(isConfirmed);
        return dto;
    }
}
