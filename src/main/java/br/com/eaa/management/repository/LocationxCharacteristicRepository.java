package br.com.eaa.management.repository;

import br.com.eaa.management.model.Location;
import br.com.eaa.management.model.LocationxCharacteristic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface LocationxCharacteristicRepository extends CrudRepository<LocationxCharacteristic, Long> {
    List<LocationxCharacteristic> findAllByLocation(Location location);
    void deleteAllByLocation(Location location);
}
