package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.DogOwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogOwnerService {

    private final DogOwnerRepository dogOwnerRepository;

    private static final Logger logger = LoggerFactory.getLogger(DogOwnerService.class);

    public DogOwnerService(DogOwnerRepository dogOwnerRepository) {
        this.dogOwnerRepository = dogOwnerRepository;
    }


    /** Метод на вход принимает сущность "усыновителя" животного и сохраняет ее в базу.
     *
     * @param dogOwner  "усыновитель" или сотрудник приюта животных.
     */
    public void creatDogOwner(DogOwner dogOwner){
        dogOwnerRepository.save(dogOwner);
    }

    /** Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
     */
    public List<DogOwner> getAllDogOwner(){
        return dogOwnerRepository.findAll();
    }

    /** Метод изменения статуса "усыновителя" собаки.
     * @param idDO id "усыновителя" собаки.
     */
    public void changeStatusOwnerById(Integer idDO, StatusOwner status) {
        dogOwnerRepository.findById(idDO).get().setStatusOwner(status);
    }

    /** Метод добавления собаки (или замены) к "усыновителю".
     * @param idDO id "усыновителя" собаки.
     */
    public void changeDogByIdDO(Integer idDO, Dog dog) {
        dogOwnerRepository.findById(idDO).get().setDog(dog);
    }

    /** Метод удаления у "усыновителя" пса при отказе в усыновлении с одновременной сменой статуса животного).
     * @param idDO id "усыновителя" пса.
     */
    public void takeTheDogAwayByIdDO(Integer idDO, Dog dog) {
        dogOwnerRepository.findById(idDO).get().setDog(null);
        // Смену статуса животного дописать после создания Репозитория Кошек !!!
    }

    /** Метод удаления "усыновителю" пса (а также сотрудников приюта).
     * @param idDO id "усыновителя" пса.
     */
    public void deleteDogOwnerByIdDO(Integer idDO){
        dogOwnerRepository.deleteById(idDO);
    }
}
