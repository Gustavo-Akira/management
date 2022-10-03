package br.com.eaa.management.dto.location;

import br.com.eaa.management.dto.characteristic.ReturnCharacteristicDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReturnLocationDTO {
    private Long id;
    private String name;
    private List<ReturnCharacteristicDTO> characteristicDTOS = new ArrayList<>();
}
