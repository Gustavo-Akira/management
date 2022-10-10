package br.com.eaa.management.dto.location;

import br.com.eaa.management.dto.characteristic.ReturnCharacteristicDTO;
import lombok.Data;

@Data
public class ReturnLocationxCharacteristicDTO {
    private ReturnCharacteristicDTO characteristicDTO;
    private String value;
}
