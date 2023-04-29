package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.CatReportDTO;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.service.CatReportService;
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
@RequestMapping("cats/report")
public class CatReportController {

    private final CatReportService catReportService;

    public CatReportController(CatReportService catReportService) {
        this.catReportService = catReportService;
    }

    @PostMapping
    @Operation(summary = "Добавление отчёта",
            description = "Здесь можно добавить отчёт в БД")
    public ResponseEntity addCatReport(@RequestBody CatReportDTO catReportDTO) {
        catReportService.addReport(catReportDTO);
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
    public ResponseEntity<CatReportDTO> getCatReportById(@RequestParam(name = "reportId") long reportId) {
        return ResponseEntity.ok().body(catReportService.getReportByReportId(reportId));
    }

    @GetMapping("/catId")
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
    public ResponseEntity<CatReportDTO> getCatReportByCatId(@RequestParam(name = "catId") long catId) {
        return ResponseEntity.ok().body(catReportService.getReportByCatId(catId));
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
    public ResponseEntity<List<CatReportDTO>> getReportsByDate(@RequestParam("date")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok().body(catReportService.getReportsByDate(date));
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
    public ResponseEntity<List<CatReportDTO>> getAllCatReports() {
        return ResponseEntity.ok().body(catReportService.getAllReports());
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
        catReportService.setReportAsComplete(reportId);
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
        catReportService.setReportAsIncomplete(reportId);
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
        catReportService.setReportAsInspected(reportId);
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
    public ResponseEntity deleteCatReportByReportId(@RequestParam(name = "reportId") long reportId) {
        catReportService.deleteReportByReportId(reportId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/catId")
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
    public ResponseEntity deleteCatReportByCatId(@RequestParam(name = "catId") long catId) {
        catReportService.deleteReportByCatId(catId);
        return ResponseEntity.noContent().build();
    }

}
