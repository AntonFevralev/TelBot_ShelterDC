package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.model.Cat;
import com.devsteam.getname.telbot_shelterdc.repository.CatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Сервис для работы с базой данных кошек
 */
@Service
public class CatService {

    private final CatRepository catRepository;


    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    /**
     * Сохраняет новую кошку в базу данных
     *
     * @param cat кошка
     * @return добавленная кошка
     * @throws WrongCatException при попытке добавить кошку без имени
     */
    public Cat addCat(Cat cat) {
        if (cat.getName().isBlank()) {
            throw new WrongCatException("Необходимо указать имя котика!");
        }
        catRepository.save(cat);
        return cat;
    }

    /**
     * Получает кошку из базы данных по идентификатору
     *
     * @param id идентификатор кошки
     * @return кошка с заданным id
     * @throws WrongCatException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public Cat getCat(Long id) {
        return catRepository.findById(id).orElseThrow();
    }
    /**
     * Получает из базы данных список всех кошек
     *
     * @return список кошек (List)
     */
    public Collection<Cat> getAllCats() {
        return catRepository.findAll();
    }
    /**
     * Редакирует кошку по идентификатору путем передачи в метод объекта кошка с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленную кошку в базу данных
     *
     * @param id идентификатор кошки
     * @param cat кошка с обновленными параметрами
     * @return обновленная кошка
     * @throws WrongCatException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public Cat updateCat(long id, Cat cat) {
        Cat updatedCat = catRepository.findById(id).orElseThrow();
        updatedCat.setColor(cat.getColor());
        updatedCat.setCatOwner(cat.getCatOwner());
        updatedCat.setStatus(cat.getStatus());
        updatedCat.setBreed(cat.getBreed());
        updatedCat.setDescription(cat.getDescription());
        updatedCat.setBirthYear(cat.getBirthYear());
        updatedCat.setName(cat.getName());
        catRepository.save(cat);
        return updatedCat;
    }
    /**
     * Удаляет кошку из базы данных по идентификатору
     *
     * @param id идентификатор кошки
     * @throws WrongCatException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public void removeCat(long id) {
       catRepository.deleteById(id);
    }
}
