package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.DogOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.service.DogOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Класс контроллера для редактирования информации о клиентах и персонале приюта собак.
 */
@Tag(name="КЛИЕНТЫ И ПЕРСОНАЛ ПРИЮТА СОБАК", description = "Редактирование данных людей в БД приюта собак")
@RestController
@RequestMapping(value = "/dogowner")
public class DogOwnerController {
        private final DogOwnerService dogOwnerService;
        public DogOwnerController(DogOwnerService dogOwnerService) {
            this.dogOwnerService = dogOwnerService;
        }
    @PostMapping
    @Operation(summary = "Добавление нового человека в БД приюта собак")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек добавлен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public ResponseEntity<DogOwnerDTO> addDogOwner(
            @RequestParam (required = false, name = "Чат id человека в Telegram") Long chatId,
            @RequestParam (required = false, name = "ФИО человека") String fullName,
            @RequestParam (required = false, name = "№ сотового телефна") String phone,
            @RequestParam (required = false, name = "Адрес проживания")String address) {
        return ResponseEntity.ok().body(dogOwnerService.creatDogOwner(chatId, fullName, phone, address));
    }
    @GetMapping
    @Operation(summary = "Получение списка данных всех людей из БД приюта собак")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Список людей получен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public ResponseEntity<List<DogOwnerDTO>> getAllDogOwners(){
        return ResponseEntity.ok().body(dogOwnerService.getAllDogOwner());
    }
    @DeleteMapping
    @Operation(summary = "Удаление человека из БД приюта собак по его id")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек удалён.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void deleteDogOwnerById(@RequestParam Long idDO){
        dogOwnerService.deleteDogOwnerByIdDO(idDO);
    }
    @PutMapping("/status")
    @Operation(summary = "Изменение статуса человека в БД приюта собак по его id")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Статус человек изменен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeStatusOwner(@RequestParam Long idDO, @RequestParam StatusOwner statusOwner){
        dogOwnerService.changeStatusOwnerById(idDO, statusOwner);
    }
    @PutMapping("/add")
    @Operation(summary = "Добавление или замена пса из БД приюта в карте человека по его id с проверкой и сменой статуса пса.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное добавлено (заменено) в карту клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeDog(@RequestParam Long idDO, @RequestParam Long id){
        dogOwnerService.changeDogByIdDO(idDO, id);
    }
    @PutMapping("/delete")
    @Operation(summary = "Удаление пса из карты человека (по id человека) по какой-либо причине со сменой статуса пса.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное стерто в карте клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void takeTheDogAway(@RequestParam Long idDO){
        dogOwnerService.takeTheDogAwayByIdDO(idDO);
    }

}
