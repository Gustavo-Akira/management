package br.com.eaa.management.presenter;

import br.com.eaa.management.dto.reservation.InsertReservationDTO;
import br.com.eaa.management.dto.reservation.ReturnReservationDTO;
import br.com.eaa.management.model.Reservation;
import br.com.eaa.management.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class ReservationPresenter {

    @Autowired
    private ReservationService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/reservations")
    public Page<ReturnReservationDTO> getReturnReservationDTOS(){
        return service.getReservations(0).map(x->x.toDTO());
    }

    @PostMapping("/reservation")
    public Boolean saveReservation(@RequestBody InsertReservationDTO dto){
        return service.addReservation(dto.toReservation());
    }
}
