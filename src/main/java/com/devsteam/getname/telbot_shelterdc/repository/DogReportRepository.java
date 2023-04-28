package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogReportRepository extends JpaRepository<DogReport, Long> {
}
