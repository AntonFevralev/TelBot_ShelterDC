package com.devsteam.getname.telbot_shelterdc.controller;


import com.devsteam.getname.telbot_shelterdc.DTO.ShelterDTO;
import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс контроллера для редактирования информации о приютах и информационных сообщений бота
 */
@Tag(name="Данные о приютах", description =  "Редактирование данных приютов")
@RestController
@RequestMapping(value = "/shelter")
public class ShelterController {

private final ShelterService service;

    public ShelterController(ShelterService service) {
        this.service = service;
    }

    @PutMapping("/contacts")
    @Operation(summary = "Изменение контактов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Контакты изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            )
    }
    )
    public ResponseEntity<Shelter> editShelterContacts(
            @RequestParam(name="ID приюта. Собаки:1. Кошки:2") int ID,
            @RequestParam(required = false, name = "Адрес приюта") String address,
            @RequestParam(required = false, name = "Режим работы") String schedule,
            @RequestParam(required = false, name= "Контакт для оформления пропуска") String security,
            @RequestParam(required = false, name = "Название приюта") String title,
            @RequestParam(required = false, name = "Ссылка на карту проезда") String mapLink){
        return ResponseEntity.ok(service.editShelterAdress(ID, address, schedule, security, title, mapLink));
    }

    @PutMapping("/safetyRules")
    @Operation(summary = "Техника безопасности")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Правила изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            )
    }
    )
    public ResponseEntity<Shelter> editSafetyRules(@RequestParam(name="ID приюта. Собаки:1. Кошки:2") int id,
                                                   @RequestParam(name="Техника безопасности") String safetyPrescriptions){
        return ResponseEntity.ok(service.editSafetyRules(id, safetyPrescriptions));
    }


    @PutMapping("/edit")
    @Operation(summary = "Изменение контактов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Контакты изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            )
    }
    )
    public ResponseEntity<Shelter> editShelter(@RequestBody ShelterDTO shelterDTO){
        return ResponseEntity.ok(service.editShelter(shelterDTO));
    }
}