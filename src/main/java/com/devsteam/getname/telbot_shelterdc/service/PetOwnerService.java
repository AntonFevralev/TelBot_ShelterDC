package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoOwnerWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.OwnerListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.exception.PetIsNotFreeException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.Utils.stringValidation;
import static com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO.petOwnerToDTO;
import static com.devsteam.getname.telbot_shelterdc.model.Status.BUSY;
import static com.devsteam.getname.telbot_shelterdc.model.Status.FREE;
import static com.devsteam.getname.telbot_shelterdc.model.StatusOwner.*;

/**
 * Сервис-класс бизнес-логики манипуляции с данными владельцев животных и других людей, связанных с приютом.
 */
@Service
public class PetOwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final TelegramBot telegramBot;

    public PetOwnerService(OwnerRepository ownerRepository, PetRepository petRepository, TelegramBot telegramBot) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Метод добавления человека в БД, меняет статус животного и id его усыновителя.
     */
    public PetOwnerDTO creatPetOwner(PetOwnerDTO petOwnerDTO) {
        if (petOwnerDTO.chatId() == 0 || !(stringValidation(petOwnerDTO.fullName())
                && stringValidation(petOwnerDTO.phone())
                && stringValidation(petOwnerDTO.address()))) {
            throw new IllegalArgumentException("Данные заполнены не корректно.");
        }
        Pet pet = petRepository.findById(petOwnerDTO.petId()).orElseThrow();
        if (pet.getStatus().equals(FREE)) {
            PetOwner petOwner = new PetOwner(petOwnerDTO.chatId(), petOwnerDTO.fullName(),
                    petOwnerDTO.phone(), petOwnerDTO.address(), PROBATION, LocalDate.now().plusDays(30), pet);
            pet.setStatus(BUSY);
            pet.setPetOwner(petOwner);

            return petOwnerToDTO(ownerRepository.save(petOwner));
        } else throw new PetIsNotFreeException("Животное занято другим человеком.");
    }

    /**
     * Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
     * @return возвращает список DTO данных людей в БД, согласно полям конструктора PetOwnerDTO.
     */
    public List<PetOwnerDTO> getAllPetOwners() {
        List<PetOwner> owners = ownerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners.stream().map(PetOwnerDTO::petOwnerToDTO).collect(Collectors.toList());
        } else throw new OwnerListIsEmptyException();
    }

    /**
     * Метод изменения статуса "усыновителя" животного.
     *
     * @param idCO   номер человека в БД приюта.
     * @param status статус человека в приюте по отношению к животному.
     * @param idCO   id "усыновителя" животного.
     * @return возвращает данные человека, согласно полям конструктора PetOwner.
     * /** Метод изменения статуса "усыновителя" животного, а также уведомление "усыновителя" о смене его статуса
     */
    public PetOwner changeStatusOwnerByIdCO(Long idCO, StatusOwner status) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        owner.setStatusOwner(status);
        ownerRepository.save(owner);
        String message = "";
        switch (status) {
            case SEARCH -> message = "Постараемся подобрать Вам питомца";
            case OWNER -> message = "Вы прошли испытательный срок, поздравляем!";
            case REJECTION -> message = "Вам отказано в усыновлении, свяжитесь с волонтером";
            case BLACKLISTED -> message = "Вы внесены в черный список нашего приюта";
            case PROBATION ->
                    message = "Вам будет выдан питомец и назначен испытательный срок до " + owner.getFinishProba() +
                            " В течение испытательного срока Вы должны присылать ежедневный отчет о состоянии питомца";
        }
        telegramBot.execute(new SendMessage(owner.getChatId(), message));
        return owner;
    }

    /**
     * Метод добавления животного (или замены) из БД к "усыновителю" по id с проверкой и сменой статуса животного.
     * Если у кота FREE, то можно передавать и статус меняется на BUSY. Если нет, то Exception.
     *
     * @param idCO номер человека в БД приюта.
     * @param id   номер животного в БД приюта.
     */
    public void changePetByIdCO(Long idCO, Long id) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Pet pet = petRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (pet.getStatus().equals(FREE)) {
            pet.setStatus(BUSY);
            owner.setPet(pet);
            pet.setPetOwner(owner);
            ownerRepository.save(owner);
            petRepository.save(pet);
        } else throw new PetIsNotFreeException("Животное занято другим человеком.");
    }

    /**
     * Метод удаления у человека животного по какой-либо причине со сменой статуса животного.
     * Например, при отказе в усыновлении или при форс-мажоре. Статус человека станет SEARCH.
     * При этом статус человека меняет волонтер (в зависимости от причины) и другим методом.
     *
     * @param idCO номер человека в БД приюта.
     */
    public void takeThePetAwayByIdCO(Long idCO) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Pet pet = owner.getPet();
        pet.setStatus(Status.FREE);
        pet.setPetOwner(null);
        petRepository.save(pet);
        owner.setPet(null);
        owner.setStatusOwner(SEARCH);
        ownerRepository.save(owner);
    }

    /**
     * Метод удаления "усыновителя" животного (или сотрудника приюта) со сменой статуса животного
     * и очистке у него поля "усыновителя".
     *
     * @param idCO номер человека в БД приюта.
     */
    public void deletePetOwnerByIdCO(Long idCO) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        if (owner.getPet() != null) {
            Pet pet = owner.getPet();
            pet.setStatus(FREE);
            pet.setPetOwner(null);
            petRepository.save(pet);

        }
        ownerRepository.deleteById(idCO);
    }

    /**
     * Метод получения человека по его id.
     *
     * @param idCO номер человека в БД приюта.
     * @return возвращает DTO человека.
     */
    public PetOwnerDTO getPetOwner(Long idCO) {
        return petOwnerToDTO(ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new));
    }

    /**
     * Метод изменения Даты окончания испытательного срока. Меняет волонтер. Уведомляет "усыновителя" об изменении
     * даты окончания испытательного срока
     *
     * @param idCO     номер человека в БД приюта.
     * @param plusDays на сколько дней увеличивается старая дата завершения испытательного периода.
     */
    public PetOwner updateFinishProba(Long idCO, int plusDays) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        owner.setFinishProba(owner.getFinishProba().plusDays(plusDays));
        ownerRepository.save(owner);
        telegramBot.execute(new SendMessage(owner.getChatId(), "Вам продлен испытательный срок до "
                + owner.getFinishProba().toString()));
        return owner;
    }

}