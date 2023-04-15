package com.devsteam.getname.telbot_shelterdc.model;


import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")

public class DogReport {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private File photo;
    @Column(nullable = false)
    private String meals;
    @Column(nullable = false)
    private String wellBeingAndAdaptation;
    @Column(nullable = false)
    private String behaviorChanges;
    @Column(nullable = false)
    private LocalDateTime reportDateTime;

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
