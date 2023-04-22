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
@Tag(name="Клиенты и персонал приюта кошек", description =  "Редактирование данных людей в БД приюта")
@RestController
@RequestMapping(value = "/cat/owner")
public class CatOwnerController {
    private final CatOwnerService catOwnerService;
    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
    }
    @PostMapping
    @Operation(summary = "Добавление нового человека в БД приюта кошек")
    public ResponseEntity<CatOwner> addCatOwner(@RequestBody CatOwner catOwner) {
        catOwnerService.creatCatOwner(catOwner);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    @Operation(summary = "Получение списка данных всех людей из БД приюта кошек")
    public ResponseEntity<List<CatOwner>> getAllCatOwners(){
        return ResponseEntity.ok().body(catOwnerService.getAllCatOwners());
    }
    @DeleteMapping
    @Operation(summary = "Удаление человека из БД приюта кошек по его id")
    public void deleteCatOwnerById(@RequestParam Integer idCO){
        catOwnerService.deleteCatOwnerByIdCO(idCO);
    }
    @PutMapping
    @Operation(summary = "Изменение статуса человека в БД приюта кошек по его id")
    public void changeStatusOwner(@RequestParam Integer idCO, @RequestParam StatusOwner statusOwner){
        catOwnerService.changeStatusOwnerByIdCO(idCO, statusOwner);
    }
    @PutMapping
    @Operation(summary = "Добавление или замена кота в карте человека (БД приюта кошек) по его id")
    public void changeCat(@RequestParam Integer idCO, @RequestParam Cat cat){
        catOwnerService.changeCatByIdCO(idCO, cat);
    }
    @PutMapping
    @Operation(summary = "Удаление кота из карты человека (БД приюта кошек) по id человека")
    public void takeTheCatAway(@RequestParam Integer idCO, @RequestParam Cat cat){
        catOwnerService.takeTheCatAwayByIdCO(idCO,cat);
    }

}