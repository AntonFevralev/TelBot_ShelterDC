package com.devsteam.getname.telbot_shelterdc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CatReportDTO(long id,
                           long catId,
                           long catOwnerId,
                           String photo,
                           String meals,
                           String wellBeingAndAdaptation,
                           String behaviorChanges,
                           LocalDate reportDate,
                           LocalTime reportTime,
                           boolean reportIsComplete,
                           boolean reportIsInspected) {
}
