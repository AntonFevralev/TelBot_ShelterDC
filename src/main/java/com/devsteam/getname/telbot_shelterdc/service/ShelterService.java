package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//сервис для работы с базой данных приюта
public class ShelterService {

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public void save(Shelter shelter){
        shelterRepository.save(shelter);
    }

    public Shelter getByID(int id){
        return shelterRepository.findById(1).get();
    }

}
