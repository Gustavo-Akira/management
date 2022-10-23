package br.com.eaa.management.presenter;

import br.com.eaa.management.dto.characteristic.CharacteristicDTO;
import br.com.eaa.management.dto.characteristic.CharacteristicFilterDTO;
import br.com.eaa.management.dto.location.*;
import br.com.eaa.management.dto.status.CreateStatusDTO;
import br.com.eaa.management.dto.status.DeletedStatus;
import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.LocationxCharacteristic;
import br.com.eaa.management.repository.LocationxCharacteristicRepository;
import br.com.eaa.management.service.CharacteristicService;
import br.com.eaa.management.service.LocationService;
import br.com.eaa.management.service.LocationxCharacteristicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LocationPresenter {

    @Autowired
    private LocationService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LocationxCharacteristicService locationxCharacteristicService;


    @GetMapping("/locations")
    public Page<ReturnLocationDTO> getLocations(){
        return service.getAll(0).map(location->{
            ReturnLocationDTO dto = modelMapper.map(location,ReturnLocationDTO.class);
            dto.setCharacteristicDTOS(locationxCharacteristicService.getAllByLocation(location).stream().map(x->x.toReturnLocationxCharacteristicDTO()).collect(Collectors.toList()));
            return dto;
        });
    }

    @GetMapping("/locations/filter")
    public Page<ReturnLocationDTO> getLocation(@RequestParam(name="name", required = false) String name, @RequestParam(name= "characteristic",required = false) Integer  characteristic){
        List<ReturnLocationDTO> returnLocationDTOS = new ArrayList<>();
        if(name != null){
            returnLocationDTOS.add(modelMapper.map(service.getByFilter(name),ReturnLocationDTO.class));
        }else if( characteristic != null){
            returnLocationDTOS = service.getAllWithoutPage().stream().filter(
                    x-> locationxCharacteristicService.getAllByLocation(x)
                            .stream()
                            .anyMatch(
                                    locationxCharacteristic-> locationxCharacteristic.getCharacteristic().getName().equals("computers")
                                            && locationxCharacteristic.getValue().equals(characteristic.toString())))
                    .map(x->modelMapper.map(x,ReturnLocationDTO.class)).collect(Collectors.toList());
        }else{
            return null;
        }
        returnLocationDTOS.stream().map(x->{
            x.setCharacteristicDTOS(locationxCharacteristicService.getAllByLocation(modelMapper.map(x,Location.class)).stream().map(locationxCharacteristic -> locationxCharacteristic.toReturnLocationxCharacteristicDTO()).collect(Collectors.toList()));
            return x;
        });
        return new PageImpl<>(returnLocationDTOS);
    }

    @GetMapping("/locations/{page}")
    public Page<ReturnLocationDTO> getLocations(@PathVariable int page){
        return service.getAll(page).map(location->{
            ReturnLocationDTO dto = modelMapper.map(location,ReturnLocationDTO.class);
            dto.setCharacteristicDTOS(locationxCharacteristicService.getAllByLocation(location).stream().map(x->x.toReturnLocationxCharacteristicDTO()).collect(Collectors.toList()));
            return dto;
        });
    }



    @PostMapping("/location")
    public ResponseEntity<CreateStatusDTO> saveLocation(@RequestBody InsertLocationDTO dto){
        Location location = service.add(dto.toLocation());
        dto.getCharacteristicDTOS().forEach(x->{
            locationxCharacteristicService.add(location, x);
        });
        return ResponseEntity.ok(new CreateStatusDTO(true));
    }

    @DeleteMapping("location/{id}")
    public ResponseEntity<DeletedStatus> deleteLocation(@PathVariable Long id){
        Location location = service.getOne(id);
        locationxCharacteristicService.removeAllByLocation(location);
        return ResponseEntity.ok(new DeletedStatus(service.remove(id)));
    }

    @GetMapping("location/{id}")
    public ReturnLocationDTO getLocation(@PathVariable Long id){
        Location location = service.getOne(id);
        ReturnLocationDTO dto = modelMapper.map(location, ReturnLocationDTO.class);
        dto.setCharacteristicDTOS(locationxCharacteristicService.getAllByLocation(location).stream().map(x->x.toReturnLocationxCharacteristicDTO()).collect(Collectors.toList()));
        return dto;
    }

    @PutMapping("location/{id}")
    public ReturnLocationDTO updateLocation(@RequestBody LocationDTO locationDTO,@PathVariable Long id){
        locationDTO.setId(id);
        Location location = service.update(modelMapper.map(locationDTO,Location.class));
        locationxCharacteristicService.update(locationDTO.getCharacteristicDTOS(),modelMapper.map(locationDTO,Location.class));
        ReturnLocationDTO dto =modelMapper.map(location,ReturnLocationDTO.class);
        dto.setCharacteristicDTOS(locationxCharacteristicService.getAllByLocation(location).stream().map(x->x.toReturnLocationxCharacteristicDTO()).collect(Collectors.toList()));
        return dto;
    }

}
