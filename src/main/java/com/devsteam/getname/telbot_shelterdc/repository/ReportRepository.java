package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByPet_Id(long petId);

    List<Report> findReportsByReportDateAndPet_Kind(LocalDate date, Kind kind);

    List<Report> findAllByReportDate(LocalDate date);

    List<Report> findReportsByReportDateAndAndPet_Id(LocalDate date, Long petId);
}
