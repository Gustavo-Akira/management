package br.com.eaa.management.service;

import br.com.eaa.management.model.Characteristic;
import br.com.eaa.management.repository.CharacteristicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CharacteristicService {
    @Autowired
    private CharacteristicRepository repository;


    public Characteristic getById(Long id){
        Optional<Characteristic> characteristic = repository.findById(id);
        if(!characteristic.isPresent()){
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return characteristic.get();
    }

    public Page<Characteristic> getAll(int page, int size){
        return repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    public boolean removeCharacteristic(Long id){
        Characteristic characteristic = getById(id);
        repository.delete(characteristic);
        return !repository.findById(id).isPresent();
    }

    public boolean addCharacteristic(Characteristic characteristic){
        characteristic = repository.save(characteristic);
        return repository.findById(characteristic.getId()).isPresent();
    }

    public Characteristic updateCharacteristic(Characteristic characteristic){
        Characteristic oldCharacteristic = getById(characteristic.getId());
        if(characteristic.getName() != null && !characteristic.getName().isEmpty()){
            oldCharacteristic.setName(characteristic.getName());
        }

        if(characteristic.getValue() != null && !characteristic.getValue().isEmpty()){
            oldCharacteristic.setValue(characteristic.getValue());
        }

        return repository.save(oldCharacteristic);
    }

}
