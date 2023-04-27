package com.devsteam.getname.telbot_shelterdc.controller;


import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс контроллера для редактирования информации о приютах и информационных сообщений бота
 */
@Tag(name = "Данные о приютах", description = "Редактирование данных приютов")
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
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editShelterContacts(
            @RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int ID,
            @RequestParam(required = false, name = "Адрес приюта") String address,
            @RequestParam(required = false, name = "Режим работы") String schedule,
            @RequestParam(required = false, name = "Контакт для оформления пропуска") String security,
            @RequestParam(required = false, name = "Название приюта") String title,
            @RequestParam(required = false, name = "Ссылка на карту проезда") String mapLink) {
        return ResponseEntity.ok(service.editShelterContacts(ID, address, schedule, security, title, mapLink));
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
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editSafetyRules(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                   @RequestParam(name = "Техника безопасности") String safetyPrescriptions) {
        return ResponseEntity.ok(service.editSafetyRules(id, safetyPrescriptions));
    }


    @PutMapping("/transportingRules")
    @Operation(summary = "Редактирование рекомандаций транспортировке")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рекомендации изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editTransportingRules(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                         @RequestParam(name = "Правила перевозки") String transportingRules) {
        return ResponseEntity.ok(service.editTransportingRules(id, transportingRules));
    }


    @PutMapping("/recommendations")
    @Operation(summary = "Редактирование рекомендаций по обустройству дома")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Правила изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editRecommendations(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                       @RequestParam(name = "Рекомендации по обустройству дома") String recommendations) {

        return ResponseEntity.ok(service.editRecommendations(id, recommendations));
    }

    @PutMapping("/meetAndGreetRules")
    @Operation(summary = "Редактирование рекомендаций знакомству с животными")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Правила изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editMeetAndGreetRules(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                         @RequestParam(name = "Рекомендации по знакомству с животным") String meetAndGreetRules) {

        return ResponseEntity.ok(service.editMeetAndGreetRules(id, meetAndGreetRules));

    }

    @PutMapping("/CynologistRecommendations")
    @Operation(summary = "Редактирование советов кинолога")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Советы кинолога изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editCynologistsAdvice(@RequestParam(name = "Рекомендации кинологов") String cynologistsRecommendations) {


        return ResponseEntity.ok(service.editCynologistsAdvice(cynologistsRecommendations));

    }

    @PutMapping("/docList")
    @Operation(summary = "Редактирование списка необходимых документов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список документов изменен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            )
    }
    )
    public ResponseEntity<Shelter> editDocList(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                               @RequestParam(name = "Список документов для усыновления") String docList) {

        return ResponseEntity.ok(service.editDocList(id, docList));
    }

    @PutMapping("/rejectReasonList")
    @Operation(summary = "Редактирование списка причин отказа")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список причин отказа изменен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editRejectReasonList(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                        @RequestParam(name = "Список причин для отказа в усыновлении") String rejectReasonList) {

        return ResponseEntity.ok(service.editRejectReasonList(id, rejectReasonList));
    }

    @PutMapping("/recommendedCynologists")
    @Operation(summary = "Редактирование списка рекомендованных кинологов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список кинологов изменен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            )
    }
    )
    public ResponseEntity<Shelter> editCynologistList(@RequestParam(name = "Список кинологов") String cynologistList) {

        return ResponseEntity.ok(service.editCynologistList(cynologistList));
    }


    @PutMapping("/info")
    @Operation(summary = "Редактирование информации о приюте")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Описание изменено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editDescription(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                   @RequestParam(name = "Описание приюта") String about) {

        return ResponseEntity.ok(service.editDescription(id, about));
    }
    @PutMapping("/recommendationAdult")
    @Operation(summary = "Редактирование рекомендаций для взрослых животных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рекомендации изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editRecommendationsAdult(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                   @RequestParam(name = "Рекомендации для взрослых животных") String recommendationAdults) {

        return ResponseEntity.ok(service.editRecommendationsAdult(id, recommendationAdults));
    }
    @PutMapping("/recommendationDisabled")
    @Operation(summary = "Редактирование рекомендаций для животных-инвалидов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Рекомендации изменены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> editRecommendationsDisabled(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id,
                                                            @RequestParam(name = "Рекомендации для животных-инвалидов") String recommendationDisabled) {

        return ResponseEntity.ok(service.editRecommendationsDisabled(id, recommendationDisabled));
    }


    @GetMapping("/about")
    @Operation(summary = "Получение всей информацию о приюте")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация получена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введены неверные параметры запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Приют не найден"
            )
    }
    )
    public ResponseEntity<Shelter> getDescription(@RequestParam(name = "ID приюта. Собаки:1. Кошки:2") int id) {

        return ResponseEntity.ok(service.getShelterInfo(id));
    }
}