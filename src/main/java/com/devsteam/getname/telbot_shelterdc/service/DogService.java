package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.exception.WrongDogException;
import com.devsteam.getname.telbot_shelterdc.model.Dog;
import com.devsteam.getname.telbot_shelterdc.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для работы с базой данных собак
 */
@Service
public class DogService {

    private Map<Long, Dog> dogs = new HashMap<>();

    private final DogRepository dogRepository;

    private long counter = 1;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }
    /**
     * Сохраняет новую собаку в базу данных
     *
     * @param dog собака
     * @return добавленная собака
     * @throws WrongDogException при попытке добавить собаку без имени
     */
    public Dog addDog(Dog dog) {
        if (dog.getName().isBlank()) {
            throw new WrongDogException("Необходимо указать имя песика!");
        }
        dogs.put(counter++, dog);
        dogRepository.save(dog);
        return dog;
    }
    /**
     * Получает собаку из базы данных по идентификатору
     *
     * @param id идентификатор собаки
     * @return собака с заданным id
     * @throws WrongDogException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public Dog getDog(Long id) {
        if (!dogs.containsKey(id)) {
            throw new WrongCatException("Такого песика нет в базе данных");
        }
        dogRepository.findById(id);
        return dogs.get(id);
    }
    /**
     * Получает из базы данных список всех собак
     *
     * @return список собак (List)
     */
    public Collection<Dog> getAllDogs() {
        dogRepository.findAll();
        return dogs.values();
    }
    /**
     * Редакирует собаку по идентификатору путем передачи в метод объекта кошка с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленную кошку в базу данных
     *
     * @param id идентификатор собаки
     * @param dog собака с обновленными параметрами
     * @return обновленная собака
     * @throws WrongDogException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public Dog updateDog(long id, Dog dog) {
        if (!dogs.containsKey(id)) {
            throw new WrongDogException("Такого песика нет в списке");
        }
        Dog updatedDog = dogs.get(id);
        updatedDog.setColor(dog.getColor());
        updatedDog.setDogOwner(dog.getDogOwner());
        updatedDog.setStatus(dog.getStatus());
        updatedDog.setBreed(dog.getBreed());
        updatedDog.setDescription(dog.getDescription());
        updatedDog.setBirthYear(dog.getBirthYear());
        updatedDog.setName(dog.getName());
        dogRepository.save(dog);
        return updatedDog;
    }
    /**
     * Удаляет собаку из базы данных по идентификатору
     *
     * @param id идентификатор собаки
     * @throws WrongDogException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public Dog removeDog(long id) {
        dogRepository.deleteById(id);
        return dogs.remove(id);
    }
}
