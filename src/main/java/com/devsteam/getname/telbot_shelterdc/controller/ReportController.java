package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Отчёты от владельцев",
        description = "Здесь волонтёры обрабатывают отчёты от животных, " +
                "принятые от владельцев на испытательном сроке")
@RequestMapping("pet/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @Operation(summary = "Добавление отчёта",
            description = "Здесь можно добавить отчёт в БД")
    public ResponseEntity addPetReport(@RequestBody ReportDTO reportDTO) {
        reportService.addReport(reportDTO);
        return ResponseEntity.ok().build();
    }

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

    @GetMapping("/petId")
    @Operation(summary = "Получение отчёта по id животного",
            description = "Здесь можно получить существующий в БД отчёт по id указанного в отчёте животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт найден"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Введите правильный ID животного"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт не найден"
            )
    }
    )
    public ResponseEntity<ReportDTO> getPetReportByCatId(@RequestParam(name = "petId") long petId) {
        return ResponseEntity.ok().body(reportService.getReportByCatId(petId));
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
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok().body(reportService.getReportsByDate(date));
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
    public ResponseEntity<List<ReportDTO>> getAllPetReports() {
        return ResponseEntity.ok().body(reportService.getAllReports());
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

    public ResponseEntity setReportAsComplete(@RequestParam(name = "reportId") long reportId) {
        reportService.setReportAsComplete(reportId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/isIncomplete")
    @Operation(summary = "Пометка отчёта как завершённого",
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
    @Operation(summary = "Пометка отчёта как завершённого",
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
    @Operation(summary = "Удаление отчёта по id животного",
            description = "Здесь можно удалить существующий в БД отчёт по id указанного в отчёте животного")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Отчёт помечен как завершённый"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Отчёт отсутствует в БД"
            )
    }
    )
    public ResponseEntity deletePetReportByCatId(@RequestParam(name = "petId") long petId) {
        reportService.deleteReportByCatId(petId);
        return ResponseEntity.noContent().build();
    }

}
