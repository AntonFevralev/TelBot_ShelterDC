package com.devsteam.getname.telbot_shelterdc.timer;

import com.devsteam.getname.telbot_shelterdc.model.Report;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ReminderTimer {

    private final TelegramBot telegramBot;

    private final ReportService reportService;

    private final ReportRepository reportRepository;

    private Map<Long, Report> allPetReports = new HashMap<>();


    private Map<Long, Report> PetReportsForToday = new HashMap<>();

    public ReminderTimer(TelegramBot telegramBot, ReportService reportService, ReportRepository reportRepository) {
        this.telegramBot = telegramBot;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }


    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void remind() {

//        telegramBot.execute(new SendMessage());
    }

    private Map<Long, Report> fillAllPetReports() {
        allPetReports = reportRepository
                .findAll()
                .stream()
                .filter(c -> c.getPetOwner().getStatusOwner() == StatusOwner.PROBATION)
                .collect(Collectors.toMap(cr -> cr.getPetOwner().getChatId(), Report -> Report));
        return allPetReports;
    }

    private Map<Long, Report> fillAllPetReportsForToday() {
        PetReportsForToday = reportRepository.findReportByReportDate(LocalDate.now())
                .stream()
                .filter(c -> c.getPetOwner().getStatusOwner() == StatusOwner.PROBATION)
                .collect(Collectors.toMap(cr -> cr.getPetOwner().getChatId(), Report -> Report));
        return PetReportsForToday;
    }

}
