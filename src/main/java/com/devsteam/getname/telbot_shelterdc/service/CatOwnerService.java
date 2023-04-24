package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.NoOwnerWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.OwnerListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.CatOwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.CatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.devsteam.getname.telbot_shelterdc.Utils.stringValidation;

@Service
public class CatOwnerService {

    private final CatOwnerRepository catOwnerRepository;
    private final CatRepository catRepository;

    private static final Logger logger = LoggerFactory.getLogger(CatOwnerService.class);

    public CatOwnerService(CatOwnerRepository catOwnerRepository, CatRepository catRepository) {
        this.catOwnerRepository = catOwnerRepository;
        this.catRepository = catRepository;
    }


    /** Метод на вход принимает сущность "усыновителя" животного и сохраняет ее в базу.
     *
     * @param "усыновитель" или сотрудник приюта животных.
     */
    public void creatCatOwner(Long chatId, String fullName, String phone, String address){
        if (chatId != 0 && stringValidation(fullName)
                && stringValidation(phone)
                && stringValidation(address))
           {
               catOwnerRepository.save(new CatOwner(fullName,phone,address,StatusOwner.SEARCH));
        } else throw new IllegalArgumentException("Данные человека заполнены не корректно.");
    }

    /** Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
     */
    public List<CatOwner> getAllCatOwners(){
        List<CatOwner> owners = catOwnerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners;
        } else throw new OwnerListIsEmptyException();
    }

    /** Метод изменения статуса "усыновителя" кошки.
     * @param idCO id "усыновителя" кошки.
     */
    public void changeStatusOwnerByIdCO(Integer idCO, StatusOwner status) {
        CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        owner.setStatusOwner(status);
        catOwnerRepository.save(owner);
    }

    /** Метод добавления кошки (или замены) из БД к "усыновителю по id".
     * @param idCO id "усыновителя" кошки.
     * @param id id "усыновителя" кошки.
     */
    public void changeCatByIdCO(Integer idCO, Long id) {
        CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Cat cat = catRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        owner.setCat(cat);
        catOwnerRepository.save(owner);
    }

    /** Метод удаления у "усыновителя" кота при отказе в усыновлении со сменой статуса кота.
     * @param idCO id "усыновителя" кошки.
     */
    public void takeTheCatAwayByIdCO(Integer idCO) {
        CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Cat cat = owner.getCat();
        cat.setStatus(Status.FREE);
        catRepository.save(cat);
        owner.setCat(null);
        catOwnerRepository.save(owner);

    }

    /** Метод удаления "усыновителя" кошки (а также сотрудников приюта).
     * @param idCO id "усыновителя" кошки.
     */
    public void deleteCatOwnerByIdCO(Integer idCO){
        try {
            CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow();
            Cat cat = owner.getCat();
            cat.setCatOwner(null);
            catRepository.save(cat);
            catOwnerRepository.deleteById(idCO);
        } catch (Exception e) {
            throw new IllegalArgumentException("Человека с таким id нет");
        }
    }
}