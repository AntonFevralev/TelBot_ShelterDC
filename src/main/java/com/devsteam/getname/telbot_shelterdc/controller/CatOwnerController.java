package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.CatOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;
import com.devsteam.getname.telbot_shelterdc.service.CatOwnerService;
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
@Tag(name="КЛИЕНТЫ И ПЕРСОНАЛ ПРИЮТА КОШЕК", description = "Редактирование данных людей в БД приюта кашек")
@RestController
@RequestMapping(value = "/catowner")
public class CatOwnerController {
    private final CatOwnerService catOwnerService;
    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
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
    public ResponseEntity<CatOwnerDTO>  addCatOwner(@RequestBody CatOwnerDTO catOwnerDTO) {
        return ResponseEntity.ok().body(catOwnerService.creatCatOwner(catOwnerDTO));
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
    public ResponseEntity<List<CatOwnerDTO>> getAllCatOwners(){
        return ResponseEntity.ok().body(catOwnerService.getAllCatOwners());
    }
    @DeleteMapping
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
    public void deleteCatOwnerById(@RequestParam Long idCO){
        catOwnerService.deleteCatOwnerByIdCO(idCO);
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
        catOwnerService.changeStatusOwnerByIdCO(idCO, statusOwner);
    }
    @PutMapping("/add")
    @Operation(summary = "Добавление или замена кота из БД приюта в карте человека по id человека с проверкой и сменой статуса кота.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное добавлено (заменено) в карту клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeCat(@RequestParam Long idCO, @RequestParam Long id){
        catOwnerService.changeCatByIdCO(idCO, id);
    }
    @PutMapping("/delete")
    @Operation(summary = "Удаление кота из карты человека (по id человека) по какой-либо причине со сменой статуса кота.")
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
        catOwnerService.takeTheCatAwayByIdCO(idCO);
    }

}
