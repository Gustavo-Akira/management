package br.com.eaa.management.repository;

import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.startTime>=?1 and r.endTime<=?2")
    List<Reservation> findAllByStartTime(LocalDateTime start, LocalDateTime end);
    @Query("SELECT r FROM Reservation r WHERE r.location=?1 and r.startTime>=?2 and r.endTime<=?3")
    List<Reservation> findAllByLocationAndStartTimeAndEndtime(Location location, LocalDateTime startTime, LocalDateTime endTime);

    Page<Reservation> findAllByLocator(User locator, Pageable pageable);
}
