package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.Utils;
import com.devsteam.getname.telbot_shelterdc.dto.DogDTO;
import com.devsteam.getname.telbot_shelterdc.exception.WrongCatException;
import com.devsteam.getname.telbot_shelterdc.exception.WrongDogException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.dto.DogDTO.dogToDogDTO;
import static com.devsteam.getname.telbot_shelterdc.model.Status.*;

/**
 * Сервис для работы с базой данных собак
 */
@Service
public class DogService {

    private final DogRepository dogRepository;


    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    /**
     * Сохраняет новую собаку в базу данных
     *
     * @param name        имя собаки
     * @param birthYear   год рождения собаки
     * @param breed       порода собаки
     * @param description описание собаки
     * @param color       цвет
     * @return объект dogDTO
     * @throws WrongCatException при попытке добавить кошку без имени
     */
    public DogDTO addDog(String name, String birthYear, String breed, String description, Color color) {
        Dog dog = new Dog(birthYear, name, breed, description, color, FREE);
        return dogToDogDTO(dogRepository.save(dog));
    }

    /**
     * Получает собаку из базы данных по идентификатору
     *
     * @param id идентификатор собаки
     * @return объект DogDTO с заданным id
     * @throws WrongDogException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public DogDTO getDog(Long id) {
        return dogToDogDTO(dogRepository.findById(id).orElseThrow());
    }

    /**
     * Получает из базы данных список всех собак
     *
     * @return список собак (List)
     */
    public Collection<DogDTO> getAllDogs() {
        List<Dog> dogList = dogRepository.findAll();
        return dogList.stream().map(DogDTO::dogToDogDTO).collect(Collectors.toList());
    }

    /**
     * Редакирует собаку по идентификатору путем передачи в метод объекта кошка с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленную кошку в базу данных
     *
     * @param dogDTO собака с обновленными параметрами, объект DTO
     * @return обновленная собака в виде объекта dogDTO
     * @throws WrongDogException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public DogDTO updateDog(DogDTO dogDTO) {
        Dog dog = dogRepository.findById(dogDTO.id()).orElseThrow();
        if (Utils.stringValidation(dogDTO.name())) {
            dog.setName(dogDTO.name());
        }
        if (Utils.stringValidation(dogDTO.breed())) {
            dog.setBreed(dogDTO.breed());
        }
        if (Utils.stringValidation(dogDTO.birthYear())) {
            dog.setBirthYear(dogDTO.birthYear());
        }
        if (Utils.stringValidation(dogDTO.description())) {
            dog.setDescription(dogDTO.description());
        }
        dog.setColor(dogDTO.color());
        if (dogDTO.ownerId() != 0) {
            dog.setDogOwner(dogOwnerRepository.findById(dogDTO.ownerId()).orElseThrow());
            DogOwner dogOwner = dogOwnerRepository.findById(dogDTO.ownerId()).orElseThrow();
            dogOwner.setDog(dog);
            dogOwnerRepository.save(dogOwner);
        }
        if (dogDTO.status() == FREE || dogDTO.status() == BUSY || dogDTO.status() == ADOPTED) {
            dog.setStatus(dogDTO.status());
        }

        return dogToDogDTO(dogRepository.save(dog));
    }

    /**
     * Удаляет собаку из базы данных по идентификатору
     *
     * @param id идентификатор собаки
     * @throws WrongDogException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public void removeDog(long id) {
        Dog dog = dogRepository.findById(id).orElseThrow();
        DogOwner dogOwner = dog.getDogOwner();
        dogOwner.setDog(null);
        dogOwnerRepository.save(dogOwner);
        dogRepository.deleteById(id);
    }
}

