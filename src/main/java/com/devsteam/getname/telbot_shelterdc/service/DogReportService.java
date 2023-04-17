package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.model.DogReport;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DogReportService {
    private final LinkedList<DogReport> dogReports = new LinkedList<>();

    public void addReport(DogReport dogReport){
        if(dogReport !=null){
            dogReports.add(dogReport);
        }
    }

    public DogReport getReportById(long id){
        return dogReports.stream().filter(r -> r.getId() == id).findAny().get();
    }

    public DogReport getReportByDate(LocalDate date){
        return dogReports.stream().filter(r -> r.getReportDateTime().toLocalDate().equals(date)).findAny().get();
    }

    public LinkedList<DogReport> getAllReports(){
        if (!dogReports.isEmpty()){
            return (LinkedList<DogReport>) List.copyOf(dogReports);
        }
        throw new ReportListIsEmptyException();
    }
}
