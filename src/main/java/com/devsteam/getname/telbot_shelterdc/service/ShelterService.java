package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

//сервис для работы с базой данных приюта

/**
 * Сервис для работы с БД приютов
 */
@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * сохраняет объект приют в БД
     * @param shelter сущность приют
     */
    public void save(Shelter shelter){
        shelterRepository.save(shelter);
    }

    /**
     * получает объект приют из БД по идентификатору
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter getByID(int id){
        return shelterRepository.findById(id).get();
    }

}
