package com.devsteam.getname.telbot_shelterdc.timer;

import com.devsteam.getname.telbot_shelterdc.config.TimeMachine;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static com.devsteam.getname.telbot_shelterdc.model.Kind.DOG;
import static java.nio.file.Files.readString;

@Component
/**
 * Класс автоматических напоминаний
 */
public class ReminderTimer {

    private final TelegramBot telegramBot;
    private final ReportService reportService;

    private final ReportRepository reportRepository;
    private final OwnerRepository ownerRepository;
    Shelter dogsShelter;
    Shelter catsShelter;


    public ReminderTimer(TelegramBot telegramBot, ReportService reportService, ReportRepository reportRepository, OwnerRepository ownerRepository, @Value("${name.of.dog.data.file}") String dogShelterFileName,
    @Value("${name.of.cat.data.file}") String catShelterFileName) throws IOException {

        try (InputStream in = Files.newInputStream(Path.of(dogShelterFileName).toAbsolutePath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            this.dogsShelter = new Gson().fromJson(reader, Shelter.class);
        }
        try (InputStream in = Files.newInputStream(Path.of(catShelterFileName).toAbsolutePath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            this.catsShelter = new Gson().fromJson(reader, Shelter.class);
        }
        this.telegramBot = telegramBot;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.ownerRepository = ownerRepository;

    }

    /**
     *Напоминает клиенту о неотправленном отчете на текущую дату
     */
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.MINUTES)
    public void remind() {

        LocalTime now = TimeMachine.now();
        if (now.truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(21, 15)
                .truncatedTo(ChronoUnit.MINUTES))) {
            List<Long> listToRemind = new ArrayList<>();
            Set<Long> allTodayReports = reportRepository.findAllByReportDate(LocalDate.now()).stream()
                    .map(r -> r.getPetOwner().getChatId()).collect(Collectors.toSet());
            List<Long> ownersChatId = ownerRepository.findAll().stream()
                    .filter(s -> s.getStatusOwner() == StatusOwner.PROBATION).map(PetOwner::getChatId).toList();
            for (Long ci : ownersChatId
            ) {
                if (!allTodayReports.contains(ci)) {
                    listToRemind.add(ci);
                }
            }
            listToRemind.forEach(ci -> {
               telegramBot.execute(new SendMessage(ci, "Отправьте, пожалуйста, отчет о животном!"));
            });

        }


    }

    /**
     * Уведомляет волонтера если клиент-усыновитель собаки 2 дня не отправлял отчет
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void remindIfTwoDaysDogOwnerDidntSendReport() {
        LocalTime now = TimeMachine.now();
        if (now.truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(21, 20)
                .truncatedTo(ChronoUnit.MINUTES))) {
            Long dogVolunteer = dogsShelter.getChatId();

            Set<Long> allDogsTodayReports = reportRepository.findReportsByReportDateAndPet_Kind(LocalDate.now(), DOG).stream()
                    .map(r -> r.getPetOwner().getIdCO())
                    .collect(Collectors.toSet());
            Set<Long> allDogsYesterdayReports = reportRepository.findReportsByReportDateAndPet_Kind(LocalDate.now().minusDays(1), DOG).stream()
                    .map(r -> r.getPetOwner().getIdCO())
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
    /**
     * Уведомляет волонтера если клиент-усыновитель кошки 2 дня не отправлял отчет
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void remindIfTwoDaysCatOwnerDidntSendReport() {
        LocalTime now = TimeMachine.now();
        if (now.truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(21, 17)
                .truncatedTo(ChronoUnit.MINUTES))) {

            Long catVolunteer = catsShelter.getChatId();

                Set<Long> allCatsTodayReports = reportRepository.findReportsByReportDateAndPet_Kind(LocalDate.now(), CAT).stream()
                        .map(r -> r.getPetOwner().getIdCO())
                        .collect(Collectors.toSet());
                Set<Long> allCatsYesterdayReports = reportRepository.findReportsByReportDateAndPet_Kind(LocalDate.now().minusDays(1),CAT).stream()
                        .map(r -> r.getPetOwner().getIdCO())
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
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void remindIfProbationFinished() {
        LocalTime now = TimeMachine.now();
        if (now.truncatedTo(ChronoUnit.MINUTES).equals(LocalTime.of(21, 10)
                .truncatedTo(ChronoUnit.MINUTES))) {
            Long catVolunteer = catsShelter.getChatId();
            Long dogVolunteer = dogsShelter.getChatId();
            Set<PetOwner> listToSend = ownerRepository.findAll().stream().filter(o->o.getFinishProba()
                    .equals(LocalDate.now())).collect(Collectors.toSet());
            List<Long> catsOwnersList = listToSend.stream().filter(o->o.getPet().getKind()==CAT).map(PetOwner::getIdCO).toList();
            List<Long> dogsOwnersList = listToSend.stream().filter(o->o.getPet().getKind()==DOG).map(PetOwner::getIdCO).toList();
            catsOwnersList.forEach(id -> telegramBot.execute(new SendMessage(catVolunteer, "У владельца с id " + id + " закончился испытательный срок")));
            dogsOwnersList.forEach(id -> telegramBot.execute(new SendMessage(dogVolunteer, "У владельца с id " + id + " закончился испытательный срок")));

        }
    }


    }