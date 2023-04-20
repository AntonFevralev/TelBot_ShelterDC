package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.service.CatReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("cats/report")
public class CatReportController {

    private final CatReportService catReportService = new CatReportService();

    @PostMapping
    public ResponseEntity addCatReport(@RequestBody CatReport catReport){
        catReportService.addReport(catReport);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reportId")
    public ResponseEntity<CatReport> getCatReportById(@RequestParam (name = "reportId") long reportId){
        return ResponseEntity.ok().body(catReportService.getReportByReportId(reportId));
    }
    @GetMapping("/catId")
    public ResponseEntity<CatReport> getCatReportByCatId(@RequestParam(name = "catId") long catId){
        return ResponseEntity.ok().body(catReportService.getReportByCatId(catId));
    }
    @GetMapping("/date")
    public ResponseEntity<List<CatReport>> getReportsByDate(@RequestParam(name = "date") LocalDate date){
        return ResponseEntity.ok().body(catReportService.getReportByDate(date));
    }

    @GetMapping
    public ResponseEntity<List<CatReport>> getAllCatReports(){
        return ResponseEntity.ok().body(catReportService.getAllReports());
    }

    @DeleteMapping("/catId")
    public ResponseEntity deleteCatReportByCatId(@RequestParam(name = "catId") long catId){
        catReportService.deleteReportByCatId(catId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reportId")
    public ResponseEntity deleteCatReportByReportId(@RequestParam(name = "reportId") long reportId){
        catReportService.deleteReportByReportId(reportId);
        return ResponseEntity.noContent().build();
    }



}
