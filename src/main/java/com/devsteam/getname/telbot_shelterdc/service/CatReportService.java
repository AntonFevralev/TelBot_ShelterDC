package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.repository.CatReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
@Service
public class CatReportService {
    private final LinkedList<CatReport> catReports = new LinkedList<>();

    private CatReportRepository catReportRepository;

    public void save(CatReport catReport){
        catReportRepository.save(catReport);
    }

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
