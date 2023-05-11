package com.devsteam.getname.telbot_shelterdc.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReportDTO(long id,
                        long petId,
                        long petOwnerId,
                        String photo,
                        String mealsWellBeingAndAdaptationBehaviorChanges,
                        LocalDate reportDate,
                        LocalTime reportTime,
                        boolean reportIsComplete,
                        boolean reportIsInspected,
                        byte[]photoInBytes) {

}
