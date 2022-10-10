package br.com.eaa.management.service;

import br.com.eaa.management.exceptions.NotFoundResourceException;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        reservation = repository.save(reservation);
        return repository.findById(reservation.getId()).isPresent();
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

        return repository.save(old);
    }

    public boolean removeReservation(Long id){
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }
}
