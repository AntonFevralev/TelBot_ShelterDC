package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.NoOwnerWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.OwnerListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.DogOwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.DogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.devsteam.getname.telbot_shelterdc.Utils.stringValidation;

@Service
public class DogOwnerService {

    private final DogOwnerRepository dogOwnerRepository;
    private final DogRepository dogRepository;

    private static final Logger logger = LoggerFactory.getLogger(DogOwnerService.class);

    public DogOwnerService(DogOwnerRepository dogOwnerRepository, DogRepository dogRepository) {
        this.dogOwnerRepository = dogOwnerRepository;
        this.dogRepository = dogRepository;
    }


    /** Метод на вход принимает сущность "усыновителя" животного и сохраняет ее в базу.
     *
     * @param  chatId человека, его ФИО, телефон и адрес.
     */
    public void creatDogOwner(Long chatId, String fullName, String phone, String address){
        if (chatId != 0 && stringValidation(fullName)
                && stringValidation(phone)
                && stringValidation(address))
        {
            dogOwnerRepository.save(new DogOwner(fullName,phone,address,StatusOwner.SEARCH));
        } else throw new IllegalArgumentException("Данные человека заполнены не корректно.");
    }

    /** Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
     */
    public List<DogOwner> getAllDogOwner(){
        List<DogOwner> owners = dogOwnerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners;
        } else throw new OwnerListIsEmptyException();
    }

    /** Метод изменения статуса "усыновителя" собаки.
     * @param idDO id "усыновителя" собаки.
     */
    public void changeStatusOwnerById(Integer idDO, StatusOwner status) {
        DogOwner owner = dogOwnerRepository.findById(idDO).orElseThrow(NoOwnerWithSuchIdException::new);
        owner.setStatusOwner(status);
        dogOwnerRepository.save(owner);
    }

    /** Метод добавления собаки (или замены) из БД к "усыновителю по id".
     * @param idDO id "усыновителя" собаки.
     *  * @param id id "усыновителя" пса.
     */
    public void changeDogByIdDO(Integer idDO, Long id) {
        DogOwner owner = dogOwnerRepository.findById(idDO).orElseThrow(NoOwnerWithSuchIdException::new);
        Dog dog = dogRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        owner.setDog(dog);
        dogOwnerRepository.save(owner);
    }

    /** Метод удаления у "усыновителя" пса при отказе в усыновлении со сменой статуса животного.
     * @param idDO id "усыновителя" пса.
     */
    public void takeTheDogAwayByIdDO(Integer idDO) {
        DogOwner owner = dogOwnerRepository.findById(idDO).orElseThrow(NoOwnerWithSuchIdException::new);
        Dog dog = owner.getDog();
        dog.setStatus(Status.FREE);
        dogRepository.save(dog);
        owner.setDog(null);
        dogOwnerRepository.save(owner);
    }

    /** Метод удаления "усыновителю" пса (а также сотрудников приюта).
     * @param idDO id "усыновителя" пса.
     */
    public void deleteDogOwnerByIdDO(Integer idDO){
        try {
            DogOwner owner = dogOwnerRepository.findById(idDO).orElseThrow();
            Dog dog = owner.getDog();
            dog.setDogOwner(null);
            dogRepository.save(dog);
            dogOwnerRepository.deleteById(idDO);
        } catch (Exception e) {
            throw new IllegalArgumentException("Человека с таким id нет");
        }
    }
}
