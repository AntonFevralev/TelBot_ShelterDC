package com.devsteam.getname.telbot_shelterdc.timer;

import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static java.nio.file.Files.readString;

@Component
public class ReminderTimer {

    private final TelegramBot telegramBot;

    private final ReportService reportService;

    private final ReportRepository reportRepository;
    private final OwnerRepository ownerRepository;
    Shelter dogsShelter;
    Shelter catsShelter;


    public ReminderTimer(TelegramBot telegramBot, ReportService reportService, ReportRepository reportRepository, OwnerRepository ownerRepository) throws IOException {
        this.telegramBot = telegramBot;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.ownerRepository = ownerRepository;
        this.dogsShelter = new Gson().fromJson(readString(Path.of("src/main/resources/", "dogShelter.json")), Shelter.class);
        this.catsShelter = new Gson().fromJson(readString(Path.of("src/main/resources/", "catShelter.json")), Shelter.class);
    }

    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.MINUTES)
    public void remind() {
        if (LocalTime.now().truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(20, 34)
                .truncatedTo(ChronoUnit.MINUTES))) {
            List<Long> listToRemind = new ArrayList<>();
            Set<Long> allTodayReports = reportRepository.findReportByReportDate(LocalDate.now()).stream()
                    .map(r -> r.getPetOwner().getChatId()).collect(Collectors.toSet());
            List<Long> ownersChatId = ownerRepository.findAll().stream()
                    .filter(s -> s.getStatusOwner() == StatusOwner.PROBATION).map(PetOwner::getChatId).toList();
            for (Long ci : ownersChatId
            ) {
                if (!allTodayReports.contains(ci)) {
                    listToRemind.add(ci);
                }
            }
            listToRemind.forEach(ci -> telegramBot.execute(new SendMessage(ci, "Отправьте, пожалуйста, отчет о животном!")));

        }


    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void remindIfTwoDaysDidntSendReport() {
        if (LocalTime.now().truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(21, 37)
                .truncatedTo(ChronoUnit.MINUTES))) {
            Long dogVolunteer = dogsShelter.getChatId();

            Set<Long> allDogsTodayReports = reportRepository.findReportByReportDate(LocalDate.now()).stream()
                    .filter(r -> r.getPetOwner().getPet().getKind() == Kind.DOG).map(r -> r.getPetOwner().getIdCO())
                    .collect(Collectors.toSet());
            Set<Long> allDogsYesterdayReports = reportRepository.findReportByReportDate(LocalDate.now().minusDays(1)).stream()
                    .filter(r -> r.getPetOwner().getPet().getKind() == Kind.DOG).map(r -> r.getPetOwner().getIdCO())
                    .collect(Collectors.toSet());
            List<Long> allDogOwnerId = ownerRepository.findAll().stream()
                    .filter(s -> s.getStatusOwner() == StatusOwner.PROBATION && s.getPet().getKind() == Kind.DOG).map(PetOwner::getChatId).toList();
            List<Long> idDogOwnerList = new ArrayList<>();
            allDogOwnerId.forEach(id -> {
                if (!allDogsTodayReports.contains(id) && !allDogsYesterdayReports.contains(id)) {
                    idDogOwnerList.add(id);
                }
            });
            idDogOwnerList.forEach(id -> telegramBot.execute(new SendMessage(dogVolunteer, "Владелец с id " + id + " не отправлял отчет 2 дня")));
        }
}
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void remindIfTwoDaysCatOwnerDidntSendReport() {
        if (LocalTime.now().truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(21, 37)
                .truncatedTo(ChronoUnit.MINUTES))) {

            Long catVolunteer = catsShelter.getChatId();

                Set<Long> allCatsTodayReports = reportRepository.findReportByReportDate(LocalDate.now()).stream()
                        .filter(r -> r.getPetOwner().getPet().getKind() == CAT).map(r -> r.getPetOwner().getIdCO())
                        .collect(Collectors.toSet());
                Set<Long> allCatsYesterdayReports = reportRepository.findReportByReportDate(LocalDate.now().minusDays(1)).stream()
                        .filter(r -> r.getPetOwner().getPet().getKind() == CAT).map(r -> r.getPetOwner().getIdCO())
                        .collect(Collectors.toSet());
                List<Long> allCatOwnerId = ownerRepository.findAll().stream()
                        .filter(s -> s.getStatusOwner() == StatusOwner.PROBATION).filter(s -> s.getPet().getKind() == CAT).map(PetOwner::getChatId).toList();
                List<Long> idCatOwnerList = new ArrayList<>();
                allCatOwnerId.forEach(id -> {
                    if (!allCatsTodayReports.contains(id) && !allCatsYesterdayReports.contains(id)) {
                        idCatOwnerList.add(id);
                    }
                });
                idCatOwnerList.forEach(id -> telegramBot.execute(new SendMessage(catVolunteer, "Владелец с id " + id + " не отправлял отчет 2 дня")));
            }
        }
    }