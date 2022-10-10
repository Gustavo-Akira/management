package br.com.eaa.management.dto.reservation;

import br.com.eaa.management.dto.location.ReturnLocationDTO;
import br.com.eaa.management.dto.user.ReturnUserDTO;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ReservationDTO {
    private Long id;
    private ReturnLocationDTO dto;
    private ReturnUserDTO userDTO;
    private LocalDate startTime;
    private LocalDate endTime;
}
