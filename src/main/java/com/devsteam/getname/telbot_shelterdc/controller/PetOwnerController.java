package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;
import com.devsteam.getname.telbot_shelterdc.service.PetOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Класс контроллера для редактирования информации о клиентах и персонале приюта кошек.
 */
@Tag(name="КЛИЕНТЫ И ПЕРСОНАЛ ПРИЮТА ЖИВОТНЫХ", description = "Редактирование данных людей в БД приюта животных")
@RestController
@RequestMapping(value = "/petowner")
public class PetOwnerController {
    private final PetOwnerService petOwnerService;
    public PetOwnerController(PetOwnerService petOwnerService) {
        this.petOwnerService = petOwnerService;
    }
    @PostMapping
    @Operation(summary = "Добавление нового человека в БД")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек добавлен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public ResponseEntity<PetOwnerDTO>  addCatOwner(@RequestBody PetOwnerDTO petOwnerDTO) {
        return ResponseEntity.ok().body(petOwnerService.creatPetOwner(petOwnerDTO));
    }
    @GetMapping
    @Operation(summary = "Получение списка данных всех людей из БД")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Список людей получен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public ResponseEntity<List<PetOwnerDTO>> getAllCatOwners(){
        return ResponseEntity.ok().body(petOwnerService.getAllPetOwners());
    }
    @DeleteMapping("{idCO}")
    @Operation(summary = "Удаление человека из БД по его id")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек удалён.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void deletePetOwnerById(@PathVariable ("idCO") Long idCO){
        petOwnerService.deletePetOwnerByIdCO(idCO);
    }
    @PutMapping("/status")
    @Operation(summary = "Изменение статуса человека в БД по его id")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Статус человек изменен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeStatusOwner(@RequestParam Long idCO, @RequestParam StatusOwner statusOwner){
        petOwnerService.changeStatusOwnerByIdCO(idCO, statusOwner);
    }
    @PutMapping("/add")
    @Operation(summary = "Добавление или замена животного из БД приюта в карте человека по id человека с проверкой и сменой статуса кота.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное добавлено (заменено) в карту клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changePet(@RequestParam Long idCO, @RequestParam Long id){
       petOwnerService.changePetByIdCO(idCO, id);
    }
    @PutMapping("/delete")
    @Operation(summary = "Удаление животного из карты человека (по id человека) по какой-либо причине со сменой статуса кота.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное стерто в карте клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void takeTheCatAway(@RequestParam Long idCO){
        petOwnerService.takeThePetAwayByIdCO(idCO);
    }

    @GetMapping("{idCO}")
    @Operation(summary = "Поиск человека по его id в приюте животных.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек найден.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public ResponseEntity<PetOwnerDTO> getPetOwner(@PathVariable ("idCO") Long idCO) {
        return ResponseEntity.ok().body(petOwnerService.getPetOwner(idCO));
    }

    @PutMapping("/proba")
    @Operation(summary = "Изменение даты завершения испытательного срока усыновителя животного")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Дата изменена.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void updateFinishProba(@RequestParam Long idCO, @RequestParam ("+N дней") int plusDays){
        petOwnerService.updateFinishProba(idCO, plusDays);
    }
}
