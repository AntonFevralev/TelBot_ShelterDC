package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.model.Cat;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CatService {

    private Map<Long, Cat> cats = new HashMap<>();

    private long counter = 1;

    public Cat addCat(Cat cat) {
        if (cat.getName().isBlank()) {
            throw new WrongCatException("Необходимо указать имя котика!");
        }
        cats.put(counter++, cat);
        return cat;
    }
    public Cat getCat(Long id) {
        if (!cats.containsKey(id)) {
            throw new WrongCatException("Такого котика нет в базе данных");
        }
        return cats.get(id);
    }
    public Collection<Cat> getAllCats() {
        return cats.values();
    }
    public Cat updateCat(long id, Cat cat) {
        Cat updatedCat = cats.get(id);
        if (updatedCat == null) {
            throw new WrongCatException("Такого котика нет в списке");
        }
        updatedCat.setColor(cat.getColor());
        updatedCat.setOwnerId(cat.getOwnerId());
        updatedCat.setStatus(cat.getStatus());
        updatedCat.setBreed(cat.getBreed());
        updatedCat.setDescription(cat.getDescription());
        updatedCat.setBirthYear(cat.getBirthYear());
        return updatedCat;
    }
    public Cat removeCat(long id) {
        return cats.remove(id);
    }
}
