package br.com.eaa.management.dto.location;

import br.com.eaa.management.dto.characteristic.CharacteristicDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LocationDTO {
    private Long id;

    private String name;

    private List<CharacteristicDTO> characteristicDTOS = new ArrayList<>();
}
