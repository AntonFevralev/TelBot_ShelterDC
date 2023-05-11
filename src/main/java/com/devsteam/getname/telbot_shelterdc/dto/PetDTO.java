package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.*;

import java.util.Objects;

public record PetDTO(Long id, int birthYear, String name, String breed, String description, Color color,
                     Status status, long ownerId, Kind kind, Gender gender) {


    public static PetDTO petToDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getBirthYear(), pet.getName(), pet.getBreed(), pet.getDescription(),
                pet.getColor(), pet.getStatus(), pet.getPetOwner()!=null? pet.getPetOwner().getIdCO() :0, pet.getKind(), pet.getGender());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetDTO petDTO = (PetDTO) o;
        return birthYear == petDTO.birthYear && ownerId == petDTO.ownerId && Objects.equals(name, petDTO.name)
                && Objects.equals(breed, petDTO.breed) && Objects.equals(description, petDTO.description)
                && color == petDTO.color && status == petDTO.status && kind == petDTO.kind && gender == petDTO.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthYear, name, breed, description, color, status, ownerId, kind, gender);
    }
}
