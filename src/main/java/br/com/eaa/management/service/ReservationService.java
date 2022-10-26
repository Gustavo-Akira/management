package br.com.eaa.management.service;

import br.com.eaa.management.dto.reservation.InsertReservationDTO;
import br.com.eaa.management.exceptions.NotFoundResourceException;
import br.com.eaa.management.exceptions.ReservationException;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.model.User;
import br.com.eaa.management.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository repository;

    @Autowired
    private LocationService service;

    @Autowired
    private UserService userService;

    public Reservation getReservation(Long id){
        return repository.findById(id).orElseThrow(()->new NotFoundResourceException(""));
    }

    public boolean addReservation(Reservation reservation){
        reservation.setLocator(userService.getById(reservation.getLocator().getId()));
        reservation.setLocation(service.getOne(reservation.getLocation().getId()));
        if(!hasReservation(reservation) && isBeforeEnd(reservation)) {
            if(reservation.getStartTime().isBefore(LocalDateTime.now().plusDays(2L).toLocalDate().atStartOfDay())){
                reservation.setIsConfirmed(true);
            }
            reservation = repository.save(reservation);
            return repository.findById(reservation.getId()).isPresent();
        }
        if(!isBeforeEnd(reservation)){
            throw new ReservationException("Reservation cannot start after it ends");
        }
        if(hasReservation(reservation)){
            throw new ReservationException("The ambient is already reserved at this hour");
        }
        return false;
    }

    public Page<Reservation> getReservations(int page){
        return repository.findAll(Pageable.ofSize(5).withPage(page));
    }

    public Reservation updateReservation(Reservation reservation){
        Reservation old = getReservation(reservation.getId());

        if(reservation.getEndTime() != null){
            old.setEndTime(reservation.getEndTime());
        }
        if(reservation.getStartTime() != null){
            old.setStartTime(reservation.getStartTime());
        }
        if(reservation.getLocation() != null){
            old.setLocation(service.getOne(reservation.getLocation().getId()));
        }
        if(reservation.getLocator() != null){
            old.setLocator(userService.getById(reservation.getLocator().getId()));
        }
        if(!hasReservation(reservation) && isBeforeEnd(reservation)) {
            removeReservation(reservation.getId());
            Reservation updated =  repository.save(old);
            updated.setLocation(service.getOne(updated.getLocation().getId()));
            updated.setLocator(userService.getById(updated.getLocator().getId()));
            return updated;
        }

        if(!isBeforeEnd(reservation)){
            throw new ReservationException("Reservation cannot start after it ends");
        }
        if(hasReservation(reservation)){
            throw new ReservationException("The ambient is already reserved at this hour");
        }

        return new Reservation();
    }

    public boolean removeReservation(Long id){
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    public boolean haveReservation(Reservation reservation){
        reservation.setLocation(service.getOne(reservation.getLocation().getId()));
        return hasReservation(reservation);
    }

    public Page<Reservation> getReservationsByUser(Integer page, User user){
        return repository.findAllByLocator(user,Pageable.ofSize(5).withPage(page));
    }

    private boolean hasReservation(Reservation reservation){
        List<Reservation> reservations = repository.findAllByLocationAndStartTimeAndEndtime(reservation.getLocation(), reservation.getStartTime().toLocalDate().atStartOfDay(), reservation.getStartTime().plusDays(1L).toLocalDate().atStartOfDay());
        AtomicBoolean y = new AtomicBoolean(false);
        reservations.forEach(x->{
            if(x.getId() != reservation.getId()) {
                if (x.getStartTime().isBefore(reservation.getStartTime()) && x.getEndTime().isAfter(reservation.getStartTime())) {
                    y.set(true);
                } else if (x.getStartTime().isAfter(reservation.getStartTime()) && x.getStartTime().isBefore(reservation.getEndTime())) {
                    y.set(true);
                } else if (x.getStartTime().equals(reservation.getStartTime()) && x.getEndTime().equals(reservation.getEndTime())) {
                    y.set(true);
                }
            }
        });
        return y.get();
    }

    public boolean confirmReservation(Long id){
        Reservation reservation = getReservation(id);
        reservation.setIsConfirmed(true);
        repository.save(reservation);
        return getReservation(id).getIsConfirmed();
    }

    private boolean isBeforeEnd(Reservation reservation){
        return reservation.getStartTime().isBefore(reservation.getEndTime());
    }
}
