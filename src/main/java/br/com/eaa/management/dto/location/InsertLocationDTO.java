package br.com.eaa.management.dto.location;

import br.com.eaa.management.dto.characteristic.CharacteristicDTO;
import br.com.eaa.management.model.Characteristic;
import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.LocationxCharacteristic;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InsertLocationDTO {

    private String name;

    private List<LocationxCharacteristicDTO> characteristicDTOS = new ArrayList<>();

    public Location toLocation(){
        Location location = new Location();
        location.setName(name);
        return location;
    }
}
