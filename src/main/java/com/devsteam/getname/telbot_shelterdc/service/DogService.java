package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.exception.WrongDogException;
import com.devsteam.getname.telbot_shelterdc.model.Dog;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class DogService {

    private Map<Long, Dog> dogs = new HashMap<>();

    private long counter = 1;

    public Dog addDog(Dog dog) {
        if (dog.getName().isBlank()) {
            throw new WrongDogException("Необходимо указать имя песика!");
        }
        dogs.put(counter++, dog);
        return dog;
    }
    public Dog getDog(Long id) {
        if (!dogs.containsKey(id)) {
            throw new WrongCatException("Такого песика нет в базе данных");
        }
        return dogs.get(id);
    }
    public Collection<Dog> getAllDogs() {
        return dogs.values();
    }
    public Dog updateDog(long id, Dog dog) {
        Dog updatedDog = dogs.get(id);
        if (updatedDog == null) {
            throw new WrongDogException("Такого песика нет в списке");
        }
        updatedDog.setColor(dog.getColor());
        updatedDog.setOwnerId(dog.getOwnerId());
        updatedDog.setStatus(dog.getStatus());
        updatedDog.setBreed(dog.getBreed());
        updatedDog.setDescription(dog.getDescription());
        updatedDog.setBirthYear(dog.getBirthYear());
        return updatedDog;
    }
    public Dog removeDog(long id) {
        return dogs.remove(id);
    }
}
