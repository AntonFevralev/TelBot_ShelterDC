package com.devsteam.getname.telbot_shelterdc.model;

import java.io.File;
import java.time.LocalDateTime;

public class Report {

    private final long id;
    private final File photo;
    private final String meals;
    private final String wellBeingAndAdaptation;
    private final String behaviorChanges;

    private final LocalDateTime reportDateTime;

    public Report(long id, File photo, String meals, String wellBeingAndAdaptation, String behaviorChanges, LocalDateTime localDateTime) {
        this.id = id;
        this.photo = photo;
        this.meals = meals;
        this.wellBeingAndAdaptation = wellBeingAndAdaptation;
        this.behaviorChanges = behaviorChanges;
        this.reportDateTime = localDateTime;
    }

    public long getId() {
        return id;
    }

    public File getPhoto() {
        return photo;
    }

    public String getMeals() {
        return meals;
    }

    public String getWellBeingAndAdaptation() {
        return wellBeingAndAdaptation;
    }

    public String getBehaviorChanges() {
        return behaviorChanges;
    }

    public LocalDateTime getReportDateTime() {
        return reportDateTime;
    }
}
