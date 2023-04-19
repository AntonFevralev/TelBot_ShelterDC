package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.NoReportWithSuchAnimalIdException;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportsOnThisDateException;
import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.repository.CatReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CatReportService {
    private final LinkedList<CatReport> catReports = new LinkedList<>();

    private CatReportRepository catReportRepository;

    public void save(CatReport catReport) {
        catReportRepository.save(catReport);
    }

    public void addReport(CatReport catReport) {
        if (catReport != null) {
            catReports.add(catReport);
        }
    }

    public CatReport getReportByReportId(long id) {
        try {
            return catReports.stream().filter(r -> r.getId() == id).findAny().get();
        } catch (NoSuchElementException e) {
            throw new NoReportWithSuchIdException();
        }
    }

    public CatReport getReportByCatId(long catId) {
        try {
            return catReports.stream().filter(report -> report.getCat().getId() == catId).findFirst().get();
        } catch (NoSuchElementException e) {
            throw new NoReportWithSuchAnimalIdException();
        }

    }

    public LinkedList<CatReport> getReportByDate(LocalDate date) {
        if (!catReports.isEmpty()) {
            return (LinkedList<CatReport>) catReports.stream().filter(r -> r.getReportDateTime().toLocalDate().equals(date)).toList();
        } else {
            throw new NoReportsOnThisDateException();
        }
    }

    public LinkedList<CatReport> getAllReports() {
        if (!catReports.isEmpty()) {
            return (LinkedList<CatReport>) List.copyOf(catReports);
        }
        throw new ReportListIsEmptyException();
    }

    public void deleteReportByCatId(long catId) {
        try {
            catReports.remove(catReports.stream().filter(report -> report.getCat().getId() == catId).findFirst().get());
        } catch (NoSuchElementException e) {
            throw new NoReportWithSuchAnimalIdException();
        }

    }

    public void deleteReportByReportId(long reportId) {
        try {
            catReports.remove(getReportByReportId(reportId));
        } catch (NullPointerException e) {
            throw new NoReportWithSuchIdException();
        }

    }
}
