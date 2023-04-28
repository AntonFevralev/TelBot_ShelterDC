package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.Pet;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.service.PetService;
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
@RequestMapping("/pets")
@Tag(name="Животные", description =  "CRUD-операции и другие эндпоинты для работы с животными")
public class PetController {

    private final PetService petService;


    public PetController(PetService petService) {
        this.petService = petService;
    }
    @GetMapping("{id}")
    @Operation(
            summary = "Поиск животного",
            description = "Поиск осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное найдено",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Животное не найдено",
                    content = {}
            )
    }
    )
    public ResponseEntity<PetDTO> getPet(@PathVariable long id) {
        return ResponseEntity.ok().body(petService.getPet(id));
    }
    @PostMapping
    @Operation(summary = "Добавление нового животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное добавлено",
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
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO) {
        return ResponseEntity.ok().body(petService.addPet(petDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление животного",
            description = "Удаление осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Животное не найдено"
            )
    }
    )
    public void deletePet(@PathVariable("id") long id) {
        petService.removePet(id);
        ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Редактирование сведений о животном",
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
    public ResponseEntity<PetDTO> updatePet(@RequestBody PetDTO petDTO) {
        return ResponseEntity.ok().body(petService.updatePet(petDTO));
    }
    @GetMapping
    @Operation(summary = "Вывод всех животных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список животных получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Pet.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Животные не найдены",
                    content = {}
            )
    }
    )
    public ResponseEntity<Collection<PetDTO>> getAllPets(@RequestParam (name = "kind")Kind kind) {
        return ResponseEntity.ok().body(petService.getAllPets(kind));
    }
}
