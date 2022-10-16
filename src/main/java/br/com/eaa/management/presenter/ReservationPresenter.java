package br.com.eaa.management.presenter;

import br.com.eaa.management.dto.reservation.InsertReservationDTO;
import br.com.eaa.management.dto.reservation.ReservationDTO;
import br.com.eaa.management.dto.reservation.ReturnReservationDTO;
import br.com.eaa.management.exceptions.SecurityException;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.security.SecurityUtility;
import br.com.eaa.management.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ReservationPresenter {

    @Autowired
    private ReservationService service;

    @Autowired
    private SecurityUtility utility;


    @GetMapping("/reservations")
    public Page<ReturnReservationDTO> getReturnReservationDTOS(){
        return service.getReservations(0).map(x->x.toDTO());
    }

    @PostMapping("/reservation")
    public Boolean saveReservation(@RequestBody InsertReservationDTO dto, Authentication authentication){
        if(!utility.isAdmin(authentication)){
            if(!utility.isSameUser(dto.getUserId(),authentication)){
                throw new SecurityException("User cannot make a reservation for another user");
            }
        }

        return service.addReservation(dto.toReservation());
    }

    @GetMapping("/reservations/{id}")
    public ReturnReservationDTO getReturnReservationDTO(@PathVariable Long id){
        return service.getReservation(id).toDTO();
    }

    @GetMapping("/reservations/{page}")
    public Page<ReturnReservationDTO> getReturnReservationDTOS(@PathVariable int page){
        return service.getReservations(page).map(x->x.toDTO());
    }

    @PutMapping("/reservations/{id}")
    public ReturnReservationDTO updateReservation(@PathVariable Long id, @RequestBody ReservationDTO dto, Authentication authentication){
        dto.setId(id);
        Reservation reservation = service.getReservation(id);
        if(!utility.isAdmin(authentication)){
            if(!utility.isSameUser(reservation.getLocator().getId(), authentication)) {
                throw new SecurityException("User cannot update a reservation of another user");
            }else if (dto.getUserId() != null) {
                if (!utility.isSameUser(dto.getUserId(), authentication)) {
                    throw new SecurityException("User cannot update a reservation for another user");
                }
            }
        }
        return service.updateReservation(dto.toReservation()).toDTO();
    }

    @DeleteMapping("/reservations/{id}")
    public Boolean deleteReservation(@PathVariable Long id, Authentication authentication){
        Reservation reservation = service.getReservation(id);
        if(!utility.isAdmin(authentication)){
            if(!utility.isSameUser(reservation.getLocator().getId(),authentication)){
                throw new SecurityException("User cannot make a reservation for another user");
            }
        }
        return service.removeReservation(id);
    }

    @GetMapping("/reservation/confirmation/{id}")
    public String confirmReservation(@PathVariable Long id){
        if(service.confirmReservation(id)){
            Reservation reservation = service.getReservation(id);
            return "Reserva confirmada com sucesso no ambiente " + reservation.getLocation().getName() + " começando as " +reservation.getStartTime() + "e terminando as " + reservation.getEndTime();
        }else {
            return "Erro durante a confirmação tente novamente";
        }
    }
}
