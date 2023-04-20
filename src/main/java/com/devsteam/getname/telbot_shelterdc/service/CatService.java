package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.model.Cat;
import com.devsteam.getname.telbot_shelterdc.repository.CatRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для работы с базой данных кошек
 */
@Service
public class CatService {

    private Map<Long, Cat> cats = new HashMap<>();

    private final CatRepository catRepository;

    private long counter = 1;

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
        cats.put(counter++, cat);
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
        if (!cats.containsKey(id)) {
            throw new WrongCatException("Такого котика нет в базе данных");
        }
        catRepository.findById(id);
        return cats.get(id);
    }
    /**
     * Получает из базы данных список всех кошек
     *
     * @return список кошек (List)
     */
    public Collection<Cat> getAllCats() {
        catRepository.findAll();
        return cats.values();
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
        if (!cats.containsKey(id)) {
            throw new WrongCatException("Такого котика нет в списке");
        }
        Cat updatedCat = cats.get(id);
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
    public Cat removeCat(long id) {
        if (!cats.containsKey(id)) {
            throw new WrongCatException("Такого котика нет в базе данных");
        }
        catRepository.deleteById(id);
        return cats.remove(id);
    }
}
