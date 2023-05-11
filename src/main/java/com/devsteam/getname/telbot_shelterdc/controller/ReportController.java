package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Отчёты от владельцев",
        description = "Здесь волонтёры обрабатывают отчёты от животных, " +
                "принятые от владельцев на испытательном сроке")
@RequestMapping("pets/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

//    @PostMapping
//    @Operation(summary = "Добавление отчёта",
//            description = "Здесь можно добавить отчёт в БД")
//    public ResponseEntity addPetReport(long chatId, String mealsWellBeingAndAdaptationBehaviorChanges, String photo) {
//        reportService.addReport(chatId, mealsWellBeingAndAdaptationBehaviorChanges, photo);
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/reportId")
    @Operation(summary = "Получение отчёта по его id",
            description = "Здесь можно получить существующий в БД отчёт по его id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт найден"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введите правильный ID"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт не найден"
            )
    }
    )
    public ResponseEntity<ReportDTO> getPetReportById(@RequestParam(name = "reportId") long reportId) {
        return ResponseEntity.ok().body(reportService.getReportByReportId(reportId));
    }
    @GetMapping("/reportIdPhoto")
    @Operation(summary = "Получение отчёта по его id",
            description = "Здесь можно получить фото, привязанное к существующему в БД отчёту по id отчёта")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт найден"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введите правильный ID"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт не найден"
            )
    }
    )
    public ResponseEntity<Resource> downloadPetReportPhotoByReportId(@RequestParam(name = "reportId") long reportId) {
        ReportDTO reportDTO = reportService.getReportByReportId(reportId);
        byte[] bytes = reportDTO.photoInBytes();
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", String.format("attachment; filename="
                +"report_id "
                +reportDTO.id()
                +" from "
                +reportDTO.reportDate()
                +" "+reportDTO.reportTime()
                +".jpg"));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/petId")
    @Operation(summary = "Получение списка отчётов по id животного",
            description = "Здесь можно получить существующие в БД отчёты по id указанного в отчёте животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёты найдены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введите правильный ID животного"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёты не найдены"
            )
    }
    )
    public ResponseEntity<List<ReportDTO>> getPetReportsByPetId(@RequestParam(name = "petId") long petId) {
        return ResponseEntity.ok().body(reportService.getReportsByPetId(petId));
    }

    @GetMapping("/chatId")
    @Operation(summary = "Получение списка отчётов по chat ID владельца" ,
            description = "Здесь можно получить существующие в БД отчёты по chat ID указанного в отчёте владельца")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёты найдены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введите правильный ID животного"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёты не найдены"
            )
    }
    )
    public ResponseEntity<List<ReportDTO>> getPetReportsByChatId(@RequestParam(name = "chatId") long chatId) {
        return ResponseEntity.ok().body(reportService.getReportsByChatId(chatId));
    }

    @GetMapping("/date")
    @Operation(summary = "Получение списка отчётов по выбранной дате",
            description = "Здесь можно получить список существующих в БД отчётов по дате их добавления")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёты найдены"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введите дату в правильном формате"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёты не найдены"
            )
    }
    )
    public ResponseEntity<List<ReportDTO>> getReportsByDate(@RequestParam("date")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                            @RequestParam("kind") Kind kind) {
        return ResponseEntity.ok().body(reportService.getReportsByDate(date, kind));
    }

    @GetMapping
    @Operation(summary = "Получение всех отчётов",
            description = "Здесь можно получить все существующие в БД отчёты")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёты найдены"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёты отсутствуют в БД"
            )
    }
    )
    public ResponseEntity<List<ReportDTO>> getAllPetReports(Kind kind) {
        return ResponseEntity.ok().body(reportService.getAllReports(kind));
    }

    @PutMapping("/isComplete")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт помечен как завершённый"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт отсутствует в БД"
            )
    }
    )
    @Operation(summary = "Пометка отчёта как завершённого",
            description = "Здесь можно пометить существующий в БД отчёт по его id как завершённый" +
                    "(соответсвующий всем требованиям к заполнению)")

    public ResponseEntity setReportAsComplete(@RequestParam(name = "reportId") long reportId, @RequestParam(name = "kind") Kind kind) {
        reportService.setReportAsComplete(reportId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/isIncomplete")
    @Operation(summary = "Пометка отчёта как НЕзавершённого",
            description = "Здесь можно пометить существующий в БД отчёт по его id как НЕзавершённый" +
                    "(НЕ соответсвующий всем требованиям к заполнению)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт помечен как НЕзавершённый"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт отсутствует в БД"
            )
    }
    )
    public ResponseEntity setReportAsIncomplete(@RequestParam(name = "reportId") long reportId) {
        reportService.setReportAsIncomplete(reportId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/isInspected")
    @Operation(summary = "Пометка отчёта как просмотренного волонтёром",
            description = "Здесь можно пометить существующий в БД отчёт по его id как просмотренный")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт помечен как просмотренный"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт отсутствует в БД"
            )
    }
    )
    public ResponseEntity setReportAsInspected(@RequestParam(name = "reportId") long reportId) {
        reportService.setReportAsInspected(reportId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reportId")
    @Operation(summary = "Удаление отчёта по его id",
            description = "Здесь можно удалить существующий в БД отчёт по его id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Отчёт успешно удалён"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт отсутствует в БД"
            )
    }
    )
    public ResponseEntity deletePetReportByReportId(@RequestParam(name = "reportId") long reportId) {
        reportService.deleteReportByReportId(reportId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/petId")
    @Operation(summary = "Удаление отчётов по id животного",
            description = "Здесь можно удалить существующие в БД отчёты по id указанного в отчёте животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Отчёты удалены"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёты отсутствует в БД"
            )
    }
    )
    public ResponseEntity deletePetReportByCatId(@RequestParam(name = "petId") long petId) {
        reportService.deleteReportsByPetId(petId);
        return ResponseEntity.noContent().build();
    }

}
