package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.exception.NoSuchShelterException;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
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
@RestController
@RequestMapping("/files/")
public class ShelterController {
  private final ShelterService service;
    @Value("${name.of.dog.data.file}")
    private String dogShelterFileName;
    @Value("${name.of.cat.data.file}")
    private String catShelterFileName;

    public ShelterController(ShelterService service) {
        this.service = service;
    }

    @Tag(name = "Выгрузить данные о приюте собак из файла JSON")
    @Operation(description = "Выберите файл с данными о приюте")
    @PostMapping(value = "/uploadDogShelter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные выгружены в приложение", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка чтения файла"
            )
    })
    public ResponseEntity<String> uploadDogShelter(@RequestParam MultipartFile file) {

        try {
            service.uploadShelterFile(file, dogShelterFileName);
        } catch (IOException e) {
            throw new NoSuchShelterException();
        }

        return ResponseEntity.ok().build();
    }

    @Tag(name = "Выгрузить данные о приюте кошек из файла JSON")
    @Operation(description = "Выберите файл с данными о приюте")
    @PostMapping(value = "/uploadCatShelter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные выгружены в приложение", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка чтения файла"
            )
    })
    public ResponseEntity<String> uploadCatShelter(@RequestParam MultipartFile file) {

        try {
            service.uploadShelterFile(file, catShelterFileName);
        } catch (IOException e) {
            throw new NoSuchShelterException();
        }

        return ResponseEntity.ok().build();
    }


    @Tag(name = "Скачать данные о приюте собак в формате JSON")
    @Operation(description = "Нажмите Download file")
    @GetMapping(value = "/downloadDogShelter")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл для загрузки готов", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет контента"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка скачивания файла"
            )
    })
    public ResponseEntity<Object> downloadDogShelterDataFile() {
        File file = service.getDataFile(dogShelterFileName);
        if (file.exists()) {
            try {
                InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentLength(file.length())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dogShelter.json\"")
                        .body(inputStreamResource);

            } catch (FileNotFoundException e) {
                    throw new NoSuchShelterException();
            }
        }
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "Скачать данные о приюте собак в формате JSON")
    @Operation(description = "Нажмите Download file")
    @GetMapping(value = "/downloadCatShelter")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл для загрузки готов", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет контента"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка скачивания файла"
            )
    })
    public ResponseEntity<Object> downloadCatShelterDataFile() {
        File file = service.getDataFile(catShelterFileName);
        if (file.exists()) {
            try {
                InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentLength(file.length())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"catShelter.json\"")
                        .body(inputStreamResource);

            } catch (FileNotFoundException e) {
                throw new NoSuchShelterException();
            }
        }
        return ResponseEntity.noContent().build();
    }

    }