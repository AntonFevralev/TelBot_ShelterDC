package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.CatReportDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportWithSuchAnimalIdException;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.NoReportsOnThisDateException;
import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.repository.CatReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CatReportService {
    //    private final LinkedList<CatReport> catReports = new LinkedList<>();
    private CatReportRepository catReportRepository;

    public CatReportService(CatReportRepository catReportRepository) {
        this.catReportRepository = catReportRepository;
    }

    private CatReportDTO catReportToDTO(CatReport catReport) {
        return new CatReportDTO(
                catReport.getId(),
                catReport.getCat().getId(),
                catReport.getCatOwner().getIdCO(),
                catReport.getPhoto(),
                catReport.getMeals(),
                catReport.getWellBeingAndAdaptation(),
                catReport.getBehaviorChanges(),
                catReport.getReportDateTime(),
                catReport.isReportIsComplete(),
                catReport.isReportIsInspected());
    }
    public void save(CatReport catReport) {
        catReportRepository.save(catReport);
    }

    /**
     * Добавить отчёт в базу
     *
     * @param catReport отчёт о кошке
     */
    public void addReport(CatReport catReport) {
        if (catReport != null) {
            save(catReport);
        }

    }

    /**
     * Получить отчёт по id отчёта
     *
     * @param id id отчёта
     * @return отчёт о кошке
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public CatReportDTO getReportByReportId(long id) {
        return catReportToDTO(catReportRepository.findById(id).orElseThrow(NoReportWithSuchIdException::new));

    }

    /**
     * Получить отчёт по id животного
     *
     * @param catId id Кошки
     * @return отчёт о кошке
     * @throws NoReportWithSuchAnimalIdException при попытке передать id несуществующего животного
     */
    public CatReportDTO getReportByCatId(long catId) {
        try {
            return catReportToDTO(catReportRepository.findCatReportByCat_Id(catId));
        } catch (NoSuchElementException e) {
            throw new NoReportWithSuchAnimalIdException();
        }

    }

    /**
     * Получить список отчётов по дате отправки
     *
     * @param date дата отправки отчётов
     * @return список отчётов на указанную дату
     * @throws NoReportsOnThisDateException при попытке передать дату, в которую не существует отчётов
     */
    public List<CatReportDTO> getReportsByDate(LocalDate date) {
        List<CatReportDTO> catReportDTOS = catReportRepository
                .findCatReportsByReportDateTime(date)
                .stream()
                .map(catReport -> catReportToDTO(catReport)).toList();
        if (!catReportDTOS.isEmpty()) {
            return catReportDTOS;
        } else {
            throw new NoReportsOnThisDateException();
        }
    }

    /**
     * Получить все отчёты
     *
     * @return список всех отчётов
     * @throws ReportListIsEmptyException если в базе нет ни одного отчёта
     */
    public List<CatReportDTO> getAllReports() {
        List<CatReportDTO> reports = catReportRepository
                .findAll()
                .stream()
                .map(catReport -> catReportToDTO(catReport))
                .toList();
        if (!reports.isEmpty()) {
            return reports;
        } else {
            throw new ReportListIsEmptyException();
        }
    }

    /**
     * Отметить отчёт как завершённый@param id id отчёта
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void setReportAsComplete(long reportId) {
        CatReport catReport = catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        catReport.setReportIsComplete(true);
        catReportRepository.save(catReport);
    }

    /**
     * Отметить отчёт как НЕзавершённый
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void setReportAsIncomplete(long reportId) {
        CatReport catReport = catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        catReport.setReportIsComplete(false);
        catReportRepository.save(catReport);
    }

    /**
     * Отметить отчёт как просмотренный
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void setReportAsInspected(long reportId) {
        CatReport catReport = catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        catReport.setReportIsInspected(true);
        catReportRepository.save(catReport);
    }

    /**
     * Удалить отчёт по id животного
     *
     * @param catId id Кошки
     * @throws NoReportWithSuchAnimalIdException при попытке передать id несуществующего животного
     */
    public void deleteReportByCatId(long catId) {
        try {
            catReportRepository.delete(catReportRepository.findCatReportByCat_Id(catId));
        } catch (IllegalArgumentException e) {
            throw new NoReportWithSuchAnimalIdException();
        }

    }

    /**
     * Удалить отчёт по id отчёта
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void deleteReportByReportId(long reportId) {
        catReportRepository.delete(catReportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new));
    }
}
