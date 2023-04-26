package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.Utils;
import com.devsteam.getname.telbot_shelterdc.dto.CatDTO;
import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.model.Cat;
import com.devsteam.getname.telbot_shelterdc.model.CatOwner;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.repository.CatOwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.CatRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.dto.CatDTO.catToCatDTO;
import static com.devsteam.getname.telbot_shelterdc.model.Status.*;

/**
 * Сервис для работы с базой данных кошек
 */
@Service
public class CatService {

    private final CatRepository catRepository;
    private final CatOwnerRepository catOwnerRepository;

    private final ShelterRepository shelterRepository;


    public CatService(CatRepository catRepository, CatOwnerRepository catOwnerRepository, ShelterRepository shelterRepository) {
        this.catRepository = catRepository;
        this.catOwnerRepository = catOwnerRepository;
        this.shelterRepository = shelterRepository;
    }

    /**
     * Сохраняет новую кошку в базу данных
     *
     * @param name имя кошки
     * @param birthYear год рождения кошки
     * @param breed порода кошки
     * @param description описание кошки
     * @param color цвет
     * @return объект catDTO
     * @throws WrongCatException при попытке добавить кошку без имени
     */
    public CatDTO addCat(String name, String birthYear, String breed, String  description, Color color) {
        Cat cat = new Cat(birthYear, name, breed, description, color, FREE);
        return catToCatDTO(catRepository.save(cat));
    }

    /**
     * Получает кошку из базы данных по идентификатору
     *
     * @param id идентификатор кошки
     * @return объект CatDTO с заданным id
     * @throws WrongCatException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public CatDTO getCat(Long id) {
        return catToCatDTO(catRepository.findById(id).orElseThrow());
    }
    /**
     * Получает из базы данных список всех кошек
     *
     * @return список кошек (List)
     */
    public Collection<CatDTO> getAllCats() {
        List<Cat> catList = catRepository.findAll();
        return catList.stream().map(CatDTO::catToCatDTO).collect(Collectors.toList());
    }
    /**
     * Редакирует кошку по идентификатору путем передачи в метод объекта кошка с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленную кошку в базу данных
     *
     * @param catDTO кошка с обновленными параметрами, объект DTO
     * @return обновленная кошка в виде объекта CatDTO
     * @throws WrongCatException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public CatDTO updateCat(CatDTO catDTO) {
        Cat cat = catRepository.findById(catDTO.id()).orElseThrow();
        if(Utils.stringValidation(catDTO.name())){
            cat.setName(catDTO.name());
        }
        if(Utils.stringValidation(catDTO.breed())){
            cat.setBreed(catDTO.breed());
        }
        if(Utils.stringValidation(catDTO.birthYear())){
            cat.setBirthYear(catDTO.birthYear());
        }
        if(Utils.stringValidation(catDTO.description())){
            cat.setDescription(catDTO.description());
        }
        cat.setColor(catDTO.color());
        if(catDTO.ownerId()!=0){
            cat.setCatOwner(catOwnerRepository.findById(catDTO.ownerId()).orElseThrow());
            CatOwner catOwner = catOwnerRepository.findById(catDTO.ownerId()).orElseThrow();
            catOwner.setCat(cat);
            catOwnerRepository.save(catOwner);
        }
        if(catDTO.status()==FREE||catDTO.status()==BUSY||catDTO.status()==ADOPTED){
            cat.setStatus(catDTO.status());
        }

        return catToCatDTO(catRepository.save(cat));
    }
    /**
     * Удаляет кошку из базы данных по идентификатору
     *
     * @param id идентификатор кошки
     * @throws WrongCatException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public void removeCat(long id) {
        Cat cat = catRepository.findById(id).orElseThrow();
        CatOwner catOwner = cat.getCatOwner();
        if(catOwner == null) {
            catRepository.deleteById(id);
        } else {
            catOwner.setCat(null);
            catOwnerRepository.save(catOwner);
            catRepository.deleteById(id);
        }
    }
    }

