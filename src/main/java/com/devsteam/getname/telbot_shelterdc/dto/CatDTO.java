package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.Cat;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;

public record CatDTO(Long id, String birthYear, String name, String breed, String description, Color color,
                     Status status, long ownerId) {


    public static CatDTO catToCatDTO(Cat cat) {
        return new CatDTO(cat.getId(), cat.getBirthYear(), cat.getName(), cat.getBreed(), cat.getDescription(),
                cat.getColor(), cat.getStatus(), cat.getCatOwner()!=null?cat.getCatOwner().getIdCO() :0);
    }
}
