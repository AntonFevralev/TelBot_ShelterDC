package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.service.CatReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("cats/report")
public class CatReportController {

    private CatReportService catReportService;

    public CatReportController(CatReportService catReportService) {
        this.catReportService = catReportService;
    }

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
    public ResponseEntity<List<CatReport>> getReportsByDate(@RequestParam("date")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok().body(catReportService.getReportsByDate(date));
    }
    @GetMapping
    public ResponseEntity<List<CatReport>> getAllCatReports(){
        return ResponseEntity.ok().body(catReportService.getAllReports());
    }

    @PutMapping("/isComplete")
    public ResponseEntity setReportAsComplete(@RequestParam(name = "reportId") long reportId){
        catReportService.setReportAsComplete(reportId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/isIncomplete")
    public ResponseEntity setReportAsIncomplete(@RequestParam(name = "reportId") long reportId){
        catReportService.setReportAsIncomplete(reportId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/isInspected")
    public ResponseEntity setReportAsInspected(@RequestParam(name = "reportId") long reportId){
        catReportService.setReportAsInspected(reportId);
        return ResponseEntity.ok().build();
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
