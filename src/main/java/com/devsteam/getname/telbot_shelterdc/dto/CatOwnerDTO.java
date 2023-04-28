package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.CatOwner;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;

public record CatOwnerDTO(Long idCO, Long chatId, String fullName, String phone, String address,
                          StatusOwner statusOwner) {


    public static CatOwnerDTO catOwnerToDTO(CatOwner catOwner){
        return new CatOwnerDTO(catOwner.getIdCO(), catOwner.getChatId(), catOwner.getFullName(), catOwner.getPhone(),
                catOwner.getAddress(), catOwner.getStatusOwner());

    }
}
