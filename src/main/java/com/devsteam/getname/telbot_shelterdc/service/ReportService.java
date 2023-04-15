package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.Report;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ReportService {
    private final LinkedList<Report> reports = new LinkedList<>();

    public void addReport(Report report){
        if(report!=null){
            reports.add(report);
        }
    }

    public Report getReportById(long id){
        return reports.stream().filter(r -> r.getId() == id).findAny().get();
    }

    public Report getReportByDate(LocalDate date){
        return reports.stream().filter(r -> r.getReportDateTime().toLocalDate().equals(date)).findAny().get();
    }

    public LinkedList<Report> getAllReports(){
        if (!reports.isEmpty()){
            return (LinkedList<Report>) List.copyOf(reports);
        }
        throw new ReportListIsEmptyException();
    }
}
