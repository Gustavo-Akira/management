package br.com.eaa.management.dto.reservation;

import br.com.eaa.management.dto.location.ReturnLocationDTO;
import br.com.eaa.management.dto.user.ReturnUserDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReturnReservationDTO {
    private Long id;
    private ReturnLocationDTO dto;
    private ReturnUserDTO userDTO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
