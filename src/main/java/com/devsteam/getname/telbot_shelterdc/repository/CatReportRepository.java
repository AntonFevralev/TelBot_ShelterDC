package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CatReportRepository extends JpaRepository<CatReport, Long> {

    CatReport findCatReportByCat_Id(long catId);

    List<CatReport> findCatReportsByReportDateTime(LocalDate date);
}
