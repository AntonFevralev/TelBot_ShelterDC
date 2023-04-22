package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.service.CatReportService;
import io.swagger.v3.oas.annotations.Operation;
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

    private CatReportService catReportService;

    public CatReportController(CatReportService catReportService) {
        this.catReportService = catReportService;
    }

    @PostMapping
    @Operation(summary = "Добавление отчёта",
            description = "Здесь можно добавить отчёт в БД")
    public ResponseEntity addCatReport(@RequestBody CatReport catReport) {
        catReportService.addReport(catReport);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reportId")
    @Operation(summary = "Получение отчёта по его id",
            description = "Здесь можно получить существующий в БД отчёт по его id")
    public ResponseEntity<CatReport> getCatReportById(@RequestParam(name = "reportId") long reportId) {
        return ResponseEntity.ok().body(catReportService.getReportByReportId(reportId));
    }

    @GetMapping("/catId")
    @Operation(summary = "Получение отчёта по id животного",
            description = "Здесь можно получить существующий в БД отчёт по id указанного в отчёте животного")
    public ResponseEntity<CatReport> getCatReportByCatId(@RequestParam(name = "catId") long catId) {
        return ResponseEntity.ok().body(catReportService.getReportByCatId(catId));
    }

    @GetMapping("/date")
    @Operation(summary = "Получение списка отчётов по выбранной дате",
            description = "Здесь можно получить список существующих в БД отчётов по дате их добавления")
    public ResponseEntity<List<CatReport>> getReportsByDate(@RequestParam("date")
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok().body(catReportService.getReportsByDate(date));
    }

    @GetMapping
    @Operation(summary = "Получение всех отчётов",
            description = "Здесь можно получить все существующие в БД отчёты")
    public ResponseEntity<List<CatReport>> getAllCatReports() {
        return ResponseEntity.ok().body(catReportService.getAllReports());
    }

    @PutMapping("/isComplete")
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
    public ResponseEntity setReportAsIncomplete(@RequestParam(name = "reportId") long reportId) {
        catReportService.setReportAsIncomplete(reportId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/isInspected")
    @Operation(summary = "Пометка отчёта как завершённого",
            description = "Здесь можно пометить существующий в БД отчёт по его id как просмотренный")
    public ResponseEntity setReportAsInspected(@RequestParam(name = "reportId") long reportId) {
        catReportService.setReportAsInspected(reportId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reportId")
    @Operation(summary = "Удаление отчёта по его id",
            description = "Здесь можно удалить существующий в БД отчёт по его id")
    public ResponseEntity deleteCatReportByReportId(@RequestParam(name = "reportId") long reportId) {
        catReportService.deleteReportByReportId(reportId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/catId")
    @Operation(summary = "Удаление отчёта по id животного",
              description = "Здесь можно удалить существующий в БД отчёт по id указанного в отчёте животного")
    public ResponseEntity deleteCatReportByCatId(@RequestParam(name = "catId") long catId) {
        catReportService.deleteReportByCatId(catId);
        return ResponseEntity.noContent().build();
    }

}
