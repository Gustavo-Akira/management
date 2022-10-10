package br.com.eaa.management.service;

import br.com.eaa.management.dto.location.LocationxCharacteristicDTO;
import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.LocationxCharacteristic;
import br.com.eaa.management.repository.LocationxCharacteristicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class LocationxCharacteristicService {

    @Autowired
    private LocationxCharacteristicRepository repository;

    @Autowired
    private CharacteristicService characteristicService;

    public void add(Location location, LocationxCharacteristicDTO x){
         LocationxCharacteristic characteristic = new LocationxCharacteristic();
         characteristic.setCharacteristic(characteristicService.getById(x.getCharacteristicId()));
         characteristic.setLocation(location);
         characteristic.setValue(x.getValue());
         repository.save(characteristic);
    }

    public List<LocationxCharacteristic> getAllByLocation(Location location){
        List<LocationxCharacteristic> locationxCharacteristics = repository.findAllByLocation(location);
        return locationxCharacteristics;
    }

    public void update(List<LocationxCharacteristicDTO> locationxCharacteristics, Location location){
        if(locationxCharacteristics.isEmpty()){
            repository.deleteAllByLocation(location);
        }else{
            repository.deleteAllByLocation(location);
            locationxCharacteristics.forEach(x->add(location,x));
        }
    }

    @Transactional
    public void removeAllByLocation(Location location){
        repository.deleteAllByLocation(location);
    }
}
