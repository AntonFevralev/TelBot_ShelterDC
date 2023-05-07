package com.devsteam.getname.telbot_shelterdc.config;

import java.time.*;

public class TimeMachine {

    private static Clock clock = Clock.systemDefaultZone();
    private static ZoneId zoneId = ZoneId.systemDefault();

    public static LocalTime now() {
        return LocalTime.now(getClock());
    }

    public static void useFixedClockAt(LocalTime date){
        clock = Clock.fixed(LocalDateTime.of(LocalDate.now(), date).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    public static void useSystemDefaultZoneClock(){
        clock = Clock.systemDefaultZone();
    }

    private static Clock getClock() {
        return clock ;
    }
}