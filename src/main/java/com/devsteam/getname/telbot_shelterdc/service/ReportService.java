package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ReportService {
    private final LinkedList<CatReport> catReports = new LinkedList<>();

    public void addReport(CatReport catReport){
        if(catReport !=null){
            catReports.add(catReport);
        }
    }

    public CatReport getReportById(long id){
        return catReports.stream().filter(r -> r.getId() == id).findAny().get();
    }

    public CatReport getReportByDate(LocalDate date){
        return catReports.stream().filter(r -> r.getReportDateTime().toLocalDate().equals(date)).findAny().get();
    }

    public LinkedList<CatReport> getAllReports(){
        if (!catReports.isEmpty()){
            return (LinkedList<CatReport>) List.copyOf(catReports);
        }
        throw new ReportListIsEmptyException();
    }
}
