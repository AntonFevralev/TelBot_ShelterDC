package com.devsteam.getname.telbot_shelterdc.dto;

import com.devsteam.getname.telbot_shelterdc.model.PetOwner;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;

/** Класс DTO владельца животного для передачи информации о нём в удобной форме.
 * @param idCO номер человека в приюте.
 * @param chatId номер человека в чате.
 * @param fullName ФИО человека.
 * @param phone № телефона.
 * @param address адрес проживания человека.
 * @param statusOwner статус человека в приюте.
 * @param id животного.
 */
public record PetOwnerDTO(Long idCO, Long chatId, String fullName, String phone, String address, StatusOwner statusOwner, Long id) {


    public static PetOwnerDTO petOwnerToDTO(PetOwner petOwner){
        return new PetOwnerDTO(petOwner.getIdPO(), petOwner.getChatId(), petOwner.getFullName(), petOwner.getPhone(),
                petOwner.getAddress(), petOwner.getStatusOwner(), petOwner.getPet() != null ? petOwner.getPet().getId() : 0L);

    }
}