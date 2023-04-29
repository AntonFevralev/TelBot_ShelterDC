package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.CatOwner;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;

/** Класс DTO владельца животного для передачи информации о нём в удобной форме.
 * @param idCO номер человека в приюте.
 * @param chatId номер человека в чате.
 * @param fullName ФИО человека.
 * @param phone № телефона.
 * @param address адрес проживания человека.
 * @param statusOwner статус человека в приюте.
 */
public record CatOwnerDTO(Long idCO, Long chatId, String fullName, String phone, String address,
                          StatusOwner statusOwner, Long id) {


    public static CatOwnerDTO catOwnerToDTO(CatOwner catOwner){
        return new CatOwnerDTO(catOwner.getIdCO(), catOwner.getChatId(), catOwner.getFullName(), catOwner.getPhone(),
                catOwner.getAddress(), catOwner.getStatusOwner(), catOwner.getCat() != null ? catOwner.getCat().getId() : 0L);

    }
}
