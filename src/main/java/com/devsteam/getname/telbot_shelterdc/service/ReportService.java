package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.exception.*;
import com.devsteam.getname.telbot_shelterdc.model.PetOwner;
import com.devsteam.getname.telbot_shelterdc.model.Report;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReportService {

    private final LinkedList<Report> petReports = new LinkedList<>();
    private ReportRepository reportRepository;
    private OwnerRepository ownerRepository;
    private PetRepository petRepository;

    public ReportService(ReportRepository reportRepository, OwnerRepository ownerRepository, PetRepository petRepository) {
        this.reportRepository = reportRepository;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    private ReportDTO petReportToDTO(Report report) {
        return new ReportDTO(
                report.getId(),
                report.getPet().getId(),
                report.getPetOwner().getIdCO(),
                report.getPhoto(),
                report.getMealsWellBeingAndAdaptationBehaviorChanges(),
                report.getReportDate(),
                report.getReportTime(),
                report.isReportIsComplete(),
                report.isReportIsInspected());
    }

    /**
     * Добавить отчёт в базу (через бота)
     *
     * @param chatId,
     * @param mealsWellBeingAndAdaptationBehaviorChanges,
     * @param photo
     *
     */
    public void addReport(long chatId, String mealsWellBeingAndAdaptationBehaviorChanges, String photo) {

        Report report = new Report();
        PetOwner owner = ownerRepository.findPetOwnerByChatId(chatId);
        report.setPetOwner(owner);
        report.setPet(owner.getPet());
        report.setMealsWellBeingAndAdaptationBehaviorChanges(mealsWellBeingAndAdaptationBehaviorChanges);
        report.setPhoto(photo);
        report.setReportIsComplete(true);
        report.setReportIsInspected(false);
        report.setReportDate(LocalDateTime.now().toLocalDate());
        report.setReportTime(LocalDateTime.now().toLocalTime());
        reportRepository.save(report);


    }

    /**
     * Получить отчёт по id отчёта
     *
     * @param id id отчёта
     * @return отчёт о кошке
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public ReportDTO getReportByReportId(long id) {
        return petReportToDTO(reportRepository.findById(id).orElseThrow(NoReportWithSuchIdException::new));

    }

    /**
     * Получить отчёт по id животного
     *
     * @param petId id Кошки
     * @return отчёт о кошке
     * @throws NoSuchEntityException при попытке передать id несуществующего животного
     */
    public ReportDTO getReportByCatId(long petId) {
        try {
            return petReportToDTO(reportRepository.findReportByPet_Id(petId));
        } catch (NoSuchElementException e) {
            throw new NoReportWithSuchPetIdException();
        }

    }

    /**
     * Получить список отчётов по дате отправки
     *
     * @param date дата отправки отчётов
     * @return список отчётов на указанную дату
     * @throws NoReportsOnThisDateException при попытке передать дату, в которую не существует отчётов
     */
    public List<ReportDTO> getReportsByDate(LocalDate date) {
        List<ReportDTO> reportDTOS = reportRepository
                .findReportByReportDate(date)
                .stream()
                .map(report -> petReportToDTO(report)).toList();
        if (!reportDTOS.isEmpty()) {
            return reportDTOS;
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
    public List<ReportDTO> getAllReports() {
        List<ReportDTO> reports = reportRepository
                .findAll()
                .stream()
                .map(report -> petReportToDTO(report))
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
        Report report = reportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        report.setReportIsComplete(true);
        reportRepository.save(report);
    }

    /**
     * Отметить отчёт как НЕзавершённый
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void setReportAsIncomplete(long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        report.setReportIsComplete(false);
        reportRepository.save(report);
    }

    /**
     * Отметить отчёт как просмотренный
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void setReportAsInspected(long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new);
        report.setReportIsInspected(true);
        reportRepository.save(report);
    }

    /**
     * Удалить отчёт по id животного
     *
     * @param petId id Кошки
     * @throws NoReportWithSuchPetIdException при попытке передать id несуществующего животного
     */
    public void deleteReportByCatId(long petId) {
        try {
            reportRepository.delete(reportRepository.findReportByPet_Id(petId));
        } catch (IllegalArgumentException e) {
            throw new NoReportWithSuchPetIdException();
        }

    }

    /**
     * Удалить отчёт по id отчёта
     *
     * @param reportId id отчёта
     * @throws NoReportWithSuchIdException при попытке передать id несуществующего отчёта
     */
    public void deleteReportByReportId(long reportId) {
        reportRepository.delete(reportRepository.findById(reportId).orElseThrow(NoReportWithSuchIdException::new));
    }
}
