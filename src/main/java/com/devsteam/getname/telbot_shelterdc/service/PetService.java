package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.Utils;
import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.exception.WrongPetException;
import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.Pet;
import com.devsteam.getname.telbot_shelterdc.model.PetOwner;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.Utils.colorValidation;
import static com.devsteam.getname.telbot_shelterdc.dto.PetDTO.petToDTO;
import static com.devsteam.getname.telbot_shelterdc.model.Status.*;

/**
 * Сервис для работы с базой данных животных
 */
@Service
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;



    public PetService(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    /**
     * Сохраняет новое животное в базу данных
     *

     * @param petDTO объект petDTO
     * @return объект petDTO
     * @throws WrongPetException при попытке добавить кошку без имени
     */
    public PetDTO addPet(PetDTO petDTO) {
        Pet pet = new Pet(petDTO.birthYear(), petDTO.name(), petDTO.breed(), petDTO.description(), petDTO.color(), FREE, petDTO.kind());
        return petToDTO(petRepository.save(pet));
    }

    /**
     * Получает животное из базы данных по идентификатору
     *
     * @param id идентификатор животного
     * @return объект PetDTO с заданным id
     * @throws WrongPetException в случае, если животного с таким идентификатором нет в базе данных
     */
    public PetDTO getPet(Long id) {
        return petToDTO(petRepository.findById(id).orElseThrow());
    }
    /**
     * Получает из базы данных список всех животных
     *
     * @return список животных (List)
     */
    public Collection<PetDTO> getAllPets(Kind kind) {
        List<Pet> petList = petRepository.findAll();
        return switch (kind) {
            case CAT -> petList.stream().filter(pet -> pet.getKind() == Kind.CAT).map(PetDTO::petToDTO).collect(Collectors.toList());
            case DOG -> petList.stream().filter(pet -> pet.getKind() == Kind.DOG).map(PetDTO::petToDTO).collect(Collectors.toList());
        };
    }
    /**
     * Редакирует животное по идентификатору путем передачи в метод объекта животное с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленное животное в базу данных
     *
     * @param petDTO животное с обновленными параметрами, объект DTO
     * @return обновленное животное в виде объекта PetDTO
     * @throws WrongPetException в случае, если животного с таким идентификатором нет в базе данных
     */
    public PetDTO updatePet(PetDTO petDTO) {
        Pet pet = petRepository.findById(petDTO.id()).orElseThrow();
        if(Utils.stringValidation(petDTO.name())){
            pet.setName(petDTO.name());
        }
        if(Utils.stringValidation(petDTO.breed())){
            pet.setBreed(petDTO.breed());
        }
        if(Utils.stringValidation(petDTO.birthYear())){
            pet.setBirthYear(petDTO.birthYear());
        }
        if(Utils.stringValidation(petDTO.description())){
            pet.setDescription(petDTO.description());
        }
        if(colorValidation(petDTO.color())){
            pet.setColor(petDTO.color());
        }
        if(petDTO.ownerId()!=0){
            pet.setPetOwner(ownerRepository.findById(petDTO.ownerId()).orElseThrow());
            PetOwner owner = ownerRepository.findById(petDTO.ownerId()).orElseThrow();
            owner.setPet(pet);
            ownerRepository.save(owner);
        }
        if(petDTO.status()==FREE||petDTO.status()==BUSY||petDTO.status()==ADOPTED){
            pet.setStatus(petDTO.status());
        }

        return petToDTO(petRepository.save(pet));
    }
    /**
     * Удаляет животное из базы данных по идентификатору
     *
     * @param id идентификатор животного
     * @throws WrongPetException в случае, если животного с таким идентификатором нет в базе данных
     */
    public void removePet(long id) {
        Pet pet = petRepository.findById(id).orElseThrow();
        PetOwner petOwner = pet.getPetOwner();
        if(petOwner == null) {
            petRepository.deleteById(id);
        } else {
            petOwner.setPet(null);
            ownerRepository.save(petOwner);
            petRepository.deleteById(id);
        }
    }
    }

