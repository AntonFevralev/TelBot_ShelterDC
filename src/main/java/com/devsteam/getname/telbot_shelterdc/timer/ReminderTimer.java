package com.devsteam.getname.telbot_shelterdc.timer;

import com.devsteam.getname.telbot_shelterdc.model.CatReport;
import com.devsteam.getname.telbot_shelterdc.model.StatusOwner;
import com.devsteam.getname.telbot_shelterdc.repository.CatReportRepository;
import com.devsteam.getname.telbot_shelterdc.service.CatReportService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
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

    private final CatReportService catReportService;

    private final CatReportRepository catReportRepository;

    private Map<Long, CatReport> allCatReports = new HashMap<>();


    private Map<Long, CatReport> catReportsForToday = new HashMap<>();

    public ReminderTimer(TelegramBot telegramBot, CatReportService catReportService, CatReportRepository catReportRepository) {
        this.telegramBot = telegramBot;
        this.catReportService = catReportService;
        this.catReportRepository = catReportRepository;
    }


    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void remind() {

        telegramBot.execute(new SendMessage())
    }

    private Map<Long, CatReport> fillAllCatReports() {
        allCatReports = catReportRepository
                .findAll()
                .stream()
                .filter(c -> c.getCatOwner().getStatusOwner() == StatusOwner.PROBATION)
                .collect(Collectors.toMap(cr -> cr.getCatOwner().getChatId(), CatReport -> CatReport));
        return allCatReports;
    }

    private Map<Long, CatReport> fillAllCatReportsForToday() {
        catReportsForToday = catReportRepository.findCatReportsByReportDate(LocalDate.now())
                .stream()
                .filter(c -> c.getCatOwner().getStatusOwner() == StatusOwner.PROBATION)
                .collect(Collectors.toMap(cr -> cr.getCatOwner().getChatId(), CatReport -> CatReport));
        return catReportsForToday;
    }

}
