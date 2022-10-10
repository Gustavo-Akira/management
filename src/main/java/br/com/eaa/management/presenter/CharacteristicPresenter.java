package br.com.eaa.management.presenter;

import br.com.eaa.management.dto.characteristic.CharacteristicDTO;
import br.com.eaa.management.dto.characteristic.InsertCharacteristicDTO;
import br.com.eaa.management.dto.characteristic.ReturnCharacteristicDTO;
import br.com.eaa.management.model.Characteristic;
import br.com.eaa.management.service.CharacteristicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CharacteristicPresenter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CharacteristicService service;

    @GetMapping("/characteristics")
    public Page<ReturnCharacteristicDTO> getAll(){
        return service.getAll(0,5).map(x->modelMapper.map(x,ReturnCharacteristicDTO.class));
    }

    @GetMapping("/characteristics/{page}")
    public Page<ReturnCharacteristicDTO> getAll(@PathVariable int page){
        return service.getAll(page,5).map(x->modelMapper.map(x,ReturnCharacteristicDTO.class));
    }

    @GetMapping("/characteristic/{id}")
    public ReturnCharacteristicDTO getOne(@PathVariable Long id){
        return modelMapper.map(service.getById(id),ReturnCharacteristicDTO.class);
    }

    @PostMapping("/characteristic")
    public ResponseEntity<Boolean> save(@RequestBody InsertCharacteristicDTO dto){
        return ResponseEntity.ok(service.addCharacteristic(modelMapper.map(dto, Characteristic.class)));
    }

    @PutMapping("/characteristic/{id}")
    public ResponseEntity<ReturnCharacteristicDTO> update(@PathVariable Long id, @RequestBody CharacteristicDTO characteristicDTO){
        characteristicDTO.setId(id);
        Characteristic characteristic = service.updateCharacteristic(modelMapper.map(characteristicDTO,Characteristic.class));
        return ResponseEntity.ok(modelMapper.map(characteristic, ReturnCharacteristicDTO.class));
    }

    @DeleteMapping("/characteristic/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        if(!service.removeCharacteristic(id)){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.noContent().build();
    }
}
