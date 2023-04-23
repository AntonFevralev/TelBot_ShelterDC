package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.model.Cat;
import com.devsteam.getname.telbot_shelterdc.model.CatOwner;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;
import com.devsteam.getname.telbot_shelterdc.service.CatOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Класс контроллера для редактирования информации о клиентах и персонале приюта кошек.
 */
@Tag(name="Клиенты и персонал приюта кошек", description =  "Редактирование данных людей в БД приюта кашек")
@RestController
@RequestMapping(value = "/cat/owner")
public class CatOwnerController {
    private final CatOwnerService catOwnerService;
    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
    }
    @PostMapping
    @Operation(summary = "Добавление нового человека в БД")
    public ResponseEntity<CatOwner> addCatOwner(@RequestBody CatOwner catOwner) { // DTO без idCO, cat, report
        catOwnerService.creatCatOwner(catOwner);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    @Operation(summary = "Получение списка данных всех людей из БД")
    public ResponseEntity<List<CatOwner>> getAllCatOwners(){
        return ResponseEntity.ok().body(catOwnerService.getAllCatOwners());
    }
    @DeleteMapping
    @Operation(summary = "Удаление человека из БД по его id")
    public void deleteCatOwnerById(@RequestParam Integer idCO){
        catOwnerService.deleteCatOwnerByIdCO(idCO);
    }
    @PutMapping
    @Operation(summary = "Изменение статуса человека в БД по его id")
    public void changeStatusOwner(@RequestParam Integer idCO, @RequestParam StatusOwner statusOwner){
        catOwnerService.changeStatusOwnerByIdCO(idCO, statusOwner);
    }
    @PutMapping
    @Operation(summary = "Добавление или замена кота в карте человека по id человека")
    public void changeCat(@RequestParam Integer idCO, @RequestParam Long id){ // DTO без ownerId
        catOwnerService.changeCatByIdCO(idCO, id);
    }
    @PutMapping
    @Operation(summary = "Удаление кота из карты человека по id человека и смена статуса кота")
    public void takeTheCatAway(@RequestParam Integer idCO){
        catOwnerService.takeTheCatAwayByIdCO(idCO);
    }

}
