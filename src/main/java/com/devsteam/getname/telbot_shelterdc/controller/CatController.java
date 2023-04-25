package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.CatDTO;
import com.devsteam.getname.telbot_shelterdc.model.Cat;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.service.CatService;
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
@RequestMapping("/cats")
@Tag(name="Кошки", description =  "CRUD-операции и другие эндпоинты для работы с кошками")
public class CatController {

    private final CatService catService;


    public CatController(CatService catService) {
        this.catService = catService;
    }
    @GetMapping("{id}")
    @Operation(
            summary = "Поиск котика",
            description = "Поиск осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Котик найден",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Котик не найден",
                    content = {}
            )
    }
    )
    public ResponseEntity<CatDTO> getCat(@PathVariable long id) {
        return ResponseEntity.ok().body(catService.getCat(id));
    }
    @PostMapping
    @Operation(summary = "Добавление нового котика")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Котик добавлен",
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
    public ResponseEntity<CatDTO> createCat(@RequestParam (name = "Имя кошки") String name,
                                         @RequestParam (required = false, name = "Год рождения кошки") String birthYear,
                                         @RequestParam (required = false, name = "Порода кошки") String breed,
                                         @RequestParam (required = false, name = "Описание кошки") String description,
                                         @RequestParam(required = false, name = "Окрас кошки") Color color) {
        return ResponseEntity.ok().body(catService.addCat(birthYear, name, breed, description, color));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление котика",
            description = "Удаление осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Котик удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Котик не найден"
            )
    }
    )
    public void deleteCat(@PathVariable("id") long id) {
        catService.removeCat(id);
        ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Редактирование сведений о котике",
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
    public ResponseEntity<CatDTO> updateCat(@RequestBody CatDTO catDTO) {
        return ResponseEntity.ok().body(catService.updateCat(catDTO));
    }
    @GetMapping
    @Operation(summary = "Вывод всех котиков")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список котиков получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Котики не найдены",
                    content = {}
            )
    }
    )
    public ResponseEntity<Collection<CatDTO>> getAllCats() {
        return ResponseEntity.ok().body(catService.getAllCats());
    }
}
