package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Dog;
import com.devsteam.getname.telbot_shelterdc.model.Status;

public record DogDTO(Long id, String birthYear, String name, String breed, String description, Color color,
                     Status status, long ownerId) {


    public static DogDTO dogToDogDTO(Dog dog) {
        return new DogDTO(dog.getId(), dog.getBirthYear(), dog.getName(), dog.getBreed(), dog.getDescription(),
                dog.getColor(), dog.getStatus(), dog.getDogOwner()!=null?dog.getDogOwner().getIdDO() :0);
    }
}
