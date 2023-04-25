package com.devsteam.getname.telbot_shelterdc.dto;

import java.time.LocalDateTime;

public record CatReportDTO(long id,
                           long catId,
                           long catOwnerId,
                           String photo,
                           String meals,
                           String wellBeingAndAdaptation,
                           String behaviorChanges,
                           LocalDateTime reportDateTime,
                           boolean reportIsComplete,
                           boolean reportIsInspected) {
}
