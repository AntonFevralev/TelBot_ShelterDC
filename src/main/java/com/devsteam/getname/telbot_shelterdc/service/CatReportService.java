package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.NoReportWithSuchAnimalIdException;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportsOnThisDateException;
import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.repository.CatReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CatReportService {
    private final LinkedList<CatReport> catReports = new LinkedList<>();

    private CatReportRepository catReportRepository;

    public CatReportService(CatReportRepository catReportRepository) {
        this.catReportRepository = catReportRepository;
    }

    public void save(CatReport catReport) {
        catReportRepository.save(catReport);
    }

    public void addReport(CatReport catReport) {
//        if (catReport != null) {
//            catReports.add(catReport);
//            save(catReport);
//        }

    }

    public CatReport getReportByReportId(long id) {
        return catReportRepository.findById(id).orElseThrow(NoReportWithSuchIdException::new);

    }

    public CatReport getReportByCatId(long catId) {
        try {
            return catReportRepository.findCatReportByCat_Id(catId);
        } catch (NoSuchElementException e) {
            throw new NoReportWithSuchAnimalIdException();
        }

    }

    public List<CatReport> getReportsByDate(LocalDate date) {
        List<CatReport> reports = catReportRepository.findCatReportsByReportDateTime(date);
        if (!reports.isEmpty()) {
            return reports;
        } else {
            throw new NoReportsOnThisDateException();
        }
    }

    public List<CatReport> getAllReports() {
        List<CatReport> reports = catReportRepository.findAll();
        if (!reports.isEmpty()) {
            return reports;
        } else {
            throw new ReportListIsEmptyException();
        }
    }

    public void setReportAsComplete(long reportId){
        CatReport catReport = catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        catReport.setReportIsComplete(true);
        catReportRepository.save(catReport);
    }

    public void setReportAsInspected(long reportId){
        CatReport catReport = catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        catReport.setReportIsInspected(true);
        catReportRepository.save(catReport);
    }

    public void deleteReportByCatId(long catId) {
        try {
            catReportRepository.delete(catReportRepository.findCatReportByCat_Id(catId));
        } catch (IllegalArgumentException e) {
            throw new NoReportWithSuchAnimalIdException();
        }

    }

    public void deleteReportByReportId(long reportId) {
        catReportRepository.delete(catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new));
    }
}
