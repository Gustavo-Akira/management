package br.com.eaa.management.model;

import br.com.eaa.management.dto.characteristic.ReturnCharacteristicDTO;
import br.com.eaa.management.dto.location.ReturnLocationxCharacteristicDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="LocationxCaracteristic ")
@Data
public class LocationxCharacteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Characteristic characteristic;

    public ReturnLocationxCharacteristicDTO toReturnLocationxCharacteristicDTO(){
        ReturnLocationxCharacteristicDTO returnLocationxCharacteristicDTO = new ReturnLocationxCharacteristicDTO();
        ReturnCharacteristicDTO returnCharacteristicDTO = new ReturnCharacteristicDTO();
        returnCharacteristicDTO.setId(characteristic.getId());
        returnCharacteristicDTO.setName(characteristic.getName());
        returnLocationxCharacteristicDTO.setCharacteristicDTO(returnCharacteristicDTO);
        returnLocationxCharacteristicDTO.setValue(value);
        return returnLocationxCharacteristicDTO;
    }
}
