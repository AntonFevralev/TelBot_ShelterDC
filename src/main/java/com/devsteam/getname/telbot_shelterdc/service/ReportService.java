package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.Utils;
import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.exception.*;
import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.PetOwner;
import com.devsteam.getname.telbot_shelterdc.model.Report;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {

    private ReportRepository reportRepository;
    private OwnerRepository ownerRepository;
    private PetRepository petRepository;
    private final TelegramBot telegramBot;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${service.file_info.uri}")
    private String fileInfoURI;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    public ReportService(ReportRepository reportRepository, OwnerRepository ownerRepository, PetRepository petRepository, TelegramBot telegramBot) {
        this.reportRepository = reportRepository;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.telegramBot = telegramBot;
    }

    private ReportDTO petReportToDTO(Report report) {
        return new ReportDTO(
                report.getId(),
                report.getPet().getId(),
                report.getPetOwner().getIdCO(),
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
     * @param photoAsBytesArray
     */
    public ReportDTO addReport(long chatId, String mealsWellBeingAndAdaptationBehaviorChanges, byte[] photoAsBytesArray) {
        Report report = new Report();
        PetOwner owner = ownerRepository.findPetOwnerByChatId(chatId);
        if (owner == null) {
            throw new NoSuchEntityException("No owner with such chat ID");
        }
        report.setPetOwner(owner);

        try {
            report.setPet(owner.getPet());
        } catch (NullPointerException e) {
            throw new PetIsNotAssignedException("this owner doesn't have assigned pet yet");
        }
        if (!Utils.stringValidation(mealsWellBeingAndAdaptationBehaviorChanges)) {
            throw new IllegalArgumentException();
        }
        report.setMealsWellBeingAndAdaptationBehaviorChanges(mealsWellBeingAndAdaptationBehaviorChanges);
        report.setReportIsComplete(true);
        report.setReportIsInspected(false);
        report.setReportDate(LocalDateTime.now().toLocalDate());
        report.setReportTime(LocalDateTime.now().toLocalTime());
        report.setPhotoAsArrayOfBytes(photoAsBytesArray);
        try {
            reportRepository.save(report);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return petReportToDTO(report);
    }

    /**
     * Получить отчёт по id отчёта
     *
     * @param id id отчёта
     * @return отчёт о питомце
     * @throws NoSuchEntityException попытке передать id несуществующего отчёта
     */
    public ReportDTO getReportByReportId(long id) {
        try {
            return petReportToDTO(reportRepository.findById(id).orElseThrow());
        } catch (Exception e) {
            throw new NoSuchEntityException("No report with such ID");
        }
    }

    public byte[] getReportPhotoBytesArray(long id) {
        return reportRepository.findById(id).get().getPhotoAsArrayOfBytes();
    }

    /**
     * Получить отчёт по id животного
     *
     * @param petId id питомца
     * @return список отчётов о питомце
     * @throws NoSuchEntityException при попытке передать id несуществующего животного
     */
    public List<ReportDTO> getReportsByPetId(long petId) {

        List<ReportDTO> reportDTOS = reportRepository.findByPet_Id(petId)
                .stream()
                .map(this::petReportToDTO)
                .toList();
        if (!reportDTOS.isEmpty()) {
            return reportDTOS;
        } else {
            throw new NoSuchEntityException("No reports with such pet ID");
        }

    }

    /**
     * Получить список отчётов по дате отправки (по типу животного)
     *
     * @param date дата отправки отчётов
     * @param kind тип животного
     * @return список отчётов на указанную дату
     * @throws NoSuchEntityException при попытке передать дату, в которую не существует отчётов
     */
    public List<ReportDTO> getReportsByDate(LocalDate date, Kind kind) {
        List<ReportDTO> reportDTOS = reportRepository
                .findReportsByReportDateAndPet_Kind(date, kind)
                .stream()
                .filter(r -> r.getPet().getKind().equals(kind))
                .map(this::petReportToDTO).toList();
        if (!reportDTOS.isEmpty()) {
            return reportDTOS;
        } else {
            throw new NoSuchEntityException("No reports on this date");
        }
    }

    /**
     * Получить все отчёты
     *
     * @return список всех отчётов по типу животного
     * @throws ReportListIsEmptyException если в базе нет ни одного отчёта
     */
    public List<ReportDTO> getAllReports(Kind kind) {
        List<ReportDTO> reports = reportRepository
                .findAll()
                .stream()
                .filter(r -> r.getPet().getKind().equals(kind))
                .map(report -> petReportToDTO(report))
                .toList();
        if (!reports.isEmpty()) {
            return reports;
        } else {
            throw new ReportListIsEmptyException();
        }
    }

    /**
     * Получить все отчёты по chat ID
     *
     * @return список всех отчётов по типу животного
     * @throws ReportListIsEmptyException если в базе нет ни одного отчёта
     */
    public List<ReportDTO> getReportsByChatId(long chatId) {
        List<ReportDTO> reports = reportRepository
                .findAll()
                .stream()
                .filter(r -> r.getPetOwner().getChatId().equals(chatId))
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
     * @throws NoSuchEntityException при попытке передать id несуществующего отчёта
     */
    public void setReportAsComplete(long reportId) {
        try {
            Report report = reportRepository.findById(reportId).orElseThrow();
            report.setReportIsComplete(true);
            reportRepository.save(report);
        } catch (Exception e) {
            throw new NoSuchEntityException("No report with such ID");
        }
    }

    /**
     * Отметить отчёт как НЕзавершённый и отправить уведомление владельцу о НЕзавершенном отчете
     *
     * @param reportId id отчёта
     * @throws NoSuchEntityException при попытке передать id несуществующего отчёта
     */
    public void setReportAsIncomplete(long reportId) {
        try {
            Report report = reportRepository.findById(reportId).orElseThrow();
            report.setReportIsComplete(false);
            reportRepository.save(report);
            telegramBot.execute(new SendMessage(report.getPetOwner().getChatId(),
                    "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                            "Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта " +
                            "будут обязаны самолично проверять условия содержания животного"));
        } catch (Exception e) {
            throw new NoSuchEntityException("No report with such ID");
        }
    }

    /**
     * Отметить отчёт как просмотренный
     *
     * @param reportId id отчёта
     * @throws NoSuchEntityException при попытке передать id несуществующего отчёта
     */
    public void setReportAsInspected(long reportId) {
        try {
            Report report = reportRepository.findById(reportId).orElseThrow();
            report.setReportIsInspected(true);
            reportRepository.save(report);
        } catch (Exception e) {
            throw new NoSuchEntityException("No report with such ID");
        }
    }

    /**
     * Удалить отчёт по id отчёта
     *
     * @param reportId id отчёта
     * @throws NoSuchEntityException при попытке передать id несуществующего отчёта
     */
    public void deleteReportByReportId(long reportId) {
        try {
            reportRepository.delete(reportRepository.findById(reportId).orElseThrow());
        } catch (Exception e) {
            throw new NoSuchEntityException("No report with such ID");
        }
    }

    /**
     * Удалить отчёты по id животного
     *
     * @param petId id животного
     * @throws NoSuchEntityException при попытке передать id несуществующего животного
     */
    public void deleteReportsByPetId(long petId) {
        List<Report> reportDTOS = reportRepository.findByPet_Id(petId);
        if (reportDTOS.isEmpty()) {
            throw new NoSuchEntityException("No report with such pet Id");
        }
        reportRepository.deleteAllInBatch(reportRepository.findByPet_Id(petId));


    }

    private byte[] downloadFile(String filePath) {
        String fullURI = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj;
        try {
            urlObj = new URL(fullURI);
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }

        try (InputStream is = urlObj.openStream()){
            return is.readAllBytes();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(fileInfoURI, HttpMethod.GET, request, String.class, token, fileId);
    }

    public byte[] processAttachment(Message telegramMessage) {
        String fileId = "";
        if (telegramMessage.photo() != null) {
            fileId = Arrays.stream(telegramMessage.photo()).max(Comparator.comparing(PhotoSize::height)).get().fileId();
        } else if (telegramMessage.document()!=null) {
            fileId = telegramMessage.document().fileId();
        }
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
            return downloadFile(filePath);
        } else {
            throw new RuntimeException("Bad response from TG service" + response);
        }
    }
}
