package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.Pet;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;

public record PetDTO(Long id, String birthYear, String name, String breed, String description, Color color,
                     Status status, long ownerId, Kind kind) {


    public static PetDTO petToDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getBirthYear(), pet.getName(), pet.getBreed(), pet.getDescription(),
                pet.getColor(), pet.getStatus(), pet.getPetOwner()!=null? pet.getPetOwner().getIdCO() :0, pet.getKind());
    }
}
