package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;

public record DogOwnerDTO(Long idDo, Long chatId, String fullName, String phone, String address,
                          StatusOwner statusOwner) {

    public static DogOwnerDTO dogOwnerToDTO(DogOwner dogOwner){
        return new DogOwnerDTO(dogOwner.getIdDO(), dogOwner.getChatId(), dogOwner.getFullName(), dogOwner.getPhone(),
                          dogOwner.getAddress(), dogOwner.getStatusOwner());

    }
}
