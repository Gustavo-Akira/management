package br.com.eaa.management.dto.reservation;

import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InsertReservationDTO {
    private Long locationId;
    private Long userId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation toReservation(){
        Reservation reservation = new Reservation();
        reservation.setEndTime(this.endTime);
        reservation.setStartTime(this.startTime);
        Location location = new Location();
        location.setId(locationId);
        User user = new User();
        user.setId(userId);
        reservation.setLocation(location);
        reservation.setLocator(user);
        return reservation;
    }
}
