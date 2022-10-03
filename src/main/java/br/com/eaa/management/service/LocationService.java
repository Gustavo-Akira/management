package br.com.eaa.management.service;

import br.com.eaa.management.model.Location;
import br.com.eaa.management.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private LocationRepository repository;

    public Page<Location> getAll(int page){
        return repository.findAll(Pageable.ofSize(5).withPage(page));
    }

    public Location getOne(Long id){
        return repository.findById(id).orElseThrow();
    }

    public boolean add(Location location){
        location = repository.save(location);
        return repository.findById(location.getId()).isPresent();
    }

    public boolean remove(Long id){
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    public Location update(Location location){
        Location old = getOne(location.getId());
        if(!location.getCharacteristics().isEmpty()){
            old.setCharacteristics(location.getCharacteristics());
        }
        if(location.getName() != null && !location.getName().isEmpty()){
            old.setName(location.getName());
        }

        return repository.save(old);
    }
}
