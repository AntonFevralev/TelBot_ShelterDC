package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.service.DogOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Класс контроллера для редактирования информации о клиентах и персонале приюта собак.
 */
@Tag(name="Клиенты и персонал приюта собак", description =  "Редактирование данных людей в БД приюта")
@RestController
@RequestMapping(value = "/dog/owner")
public class DogOwnerController {
        private final DogOwnerService dogOwnerService;

        public DogOwnerController(DogOwnerService dogOwnerService) {
            this.dogOwnerService = dogOwnerService;
        }

    @PostMapping
    @Operation(summary = "Добавление нового человека в БД приюта собак")
    public ResponseEntity<DogOwner> addDogOwner(@RequestBody DogOwner dogOwner) {
        dogOwnerService.creatDogOwner(dogOwner);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    @Operation(summary = "Получение списка данных всех людей из БД приюта собак")
    public ResponseEntity<List<DogOwner>> getAllDogOwners(){
        return ResponseEntity.ok().body(dogOwnerService.getAllDogOwner());
    }
    @DeleteMapping
    @Operation(summary = "Удаление человека из БД приюта собак по его id")
    public void deleteDogOwnerById(@RequestParam Integer idDO){
        dogOwnerService.deleteDogOwnerByIdDO(idDO);
    }
    @PutMapping
    @Operation(summary = "Изменение статуса человека в БД приюта собак по его id")
    public void changeStatusOwner(@RequestParam Integer idDO, @RequestParam StatusOwner statusOwner){
        dogOwnerService.changeStatusOwnerById(idDO, statusOwner);
    }
    @PutMapping
    @Operation(summary = "Добавление или замена пса в карте человека (БД приюта собак) по его id")
    public void changeDog(@RequestParam Integer idDO, @RequestParam Dog dog){
        dogOwnerService.changeDogByIdDO(idDO, dog);
    }
    @PutMapping
    @Operation(summary = "Удаление пса из карты человека (БД приюта собак) по id человека")
    public void takeTheDogAway(@RequestParam Integer idDO, @RequestParam Dog dog){
        dogOwnerService.takeTheDogAwayByIdDO(idDO,dog);
    }

}
