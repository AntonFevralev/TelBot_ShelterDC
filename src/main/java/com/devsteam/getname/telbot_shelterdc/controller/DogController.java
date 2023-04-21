package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.model.Dog;
import com.devsteam.getname.telbot_shelterdc.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/dogs")
@Tag(name="Собаки", description =  "CRUD-операции и другие эндпоинты для работы с собаками")
public class DogController {

    private final DogService dogService;


    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Поиск песика",
            description = "Поиск осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Песик найден",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Песик не найден",
                    content = {}
            )
    }
    )
    public ResponseEntity<Dog> getDog(@PathVariable long id) {
        return ResponseEntity.ok().body(dogService.getDog(id));
    }
    @PostMapping
    @Operation(summary = "Добавление нового песика")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Песик добавлен",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка",
                    content = {}
            )
    }
    )
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        return ResponseEntity.ok().body(dogService.addDog(dog));
    }
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление песика",
            description = "Удаление осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Песик удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Песик не найден"
            )
    }
    )
    public ResponseEntity<Dog> deleteDog(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(dogService.removeDog(id));
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Редактирование сведений о песике",
            description = "Редактирование осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Сведения отредактированы",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка",
                    content = {}
            )
    }
    )
    public ResponseEntity<Dog> updateDog(@PathVariable("id") long id, @RequestBody Dog dog) {
        return ResponseEntity.ok().body(dogService.updateDog(id, dog));
    }
    @GetMapping
    @Operation(summary = "Вывод всех песиков")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список песиков получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Песики не найдены",
                    content = {}
            )
    }
    )
    public ResponseEntity<Collection<Dog>> getAllDogs() {
        return ResponseEntity.ok().body(dogService.getAllDogs());
    }
}
