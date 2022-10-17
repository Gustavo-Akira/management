package br.com.eaa.management.dto.reservation;

import br.com.eaa.management.dto.location.ReturnLocationDTO;
import br.com.eaa.management.dto.user.ReturnUserDTO;
import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long locationId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation toReservation(){
        Reservation reservation = new Reservation();
        User user = new User();
        user.setId(userId);
        reservation.setLocator(user);
        Location location = new Location();
        location.setId(locationId);
        reservation.setLocation(location);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setId(id);
        return reservation;
    }
}
