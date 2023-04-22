package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.CatOwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatOwnerService {

    private final CatOwnerRepository catOwnerRepository;

    private static final Logger logger = LoggerFactory.getLogger(CatOwnerService.class);

    public CatOwnerService(CatOwnerRepository catOwnerRepository) {
        this.catOwnerRepository = catOwnerRepository;
    }


    /** Метод на вход принимает сущность "усыновителя" животного и сохраняет ее в базу.
     *
     * @param catOwner  "усыновитель" или сотрудник приюта животных.
     */
    public void creatCatOwner(CatOwner catOwner){
        catOwnerRepository.save(catOwner);
    }

    /** Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
    */
    public List<CatOwner> getAllCatOwner(){
        return catOwnerRepository.findAll();
    }

    /** Метод изменения статуса "усыновителя" кошки.
      * @param idCO id "усыновителя" кошки.
     */
    public void changeStatusOwnerByIdCO(Integer idCO, StatusOwner status) {
        catOwnerRepository.findById(idCO).get().setStatusOwner(status);
    }

    /** Метод добавления кошки (или замены) к "усыновителю".
     * @param idCO id "усыновителя" кошки.
     */
    public void changeCatByIdCO(Integer idCO, Cat cat) {
        catOwnerRepository.findById(idCO).get().setCat(cat);
    }

    /** Метод удаления у "усыновителя" кота при отказе в усыновлении с одновременной сменой статуса животного).
     * @param idCO id "усыновителя" кошки.
     */
    public void takeTheCatAwayByIdCO(Integer idCO, Cat cat) {
        catOwnerRepository.findById(idCO).get().setCat(null);
    // Смену статуса животного дописать после создания Репозитория Кошек !!!
    }

    /** Метод удаления "усыновителю" кошки (а также сотрудников приюта).
     * @param idCO id "усыновителя" кошки.
     */
    public void deleteCatOwnerByIdCO(Integer idCO){
        catOwnerRepository.deleteById(idCO);
    }
}

