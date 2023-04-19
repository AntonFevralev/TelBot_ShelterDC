package com.devsteam.getname.telbot_shelterdc.model;


import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;

@Entity
@Table(name = "cat_reports")

public class CatReport {
    @Id
    @GeneratedValue
    private long id;
    @OneToOne(targetEntity = Cat.class, cascade = CascadeType.ALL)
    @Column(nullable = false)
    private Cat cat;
    @ManyToOne(targetEntity = CatOwner.class, cascade = CascadeType.ALL)
    @Column(nullable = false)
    private CatOwner catOwner;
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

    @Column(nullable = false)
    private boolean reportIsComplete;
    @Column(nullable = false)
    private boolean ownerIsOnTrialPeriod;

    public long getId() {
        return id;
    }

    public Cat getCat() {
        return cat;
    }

    public CatOwner getCatOwner() {
        return catOwner;
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

    public boolean isReportIsComplete() {
        return reportIsComplete;
    }

    public boolean isOwnerIsOnTrialPeriod() {
        return ownerIsOnTrialPeriod;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public void setCatOwner(CatOwner catOwner) {
        this.catOwner = catOwner;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public void setMeals(String meals) {
        this.meals = meals;
    }

    public void setWellBeingAndAdaptation(String wellBeingAndAdaptation) {
        this.wellBeingAndAdaptation = wellBeingAndAdaptation;
    }

    public void setBehaviorChanges(String behaviorChanges) {
        this.behaviorChanges = behaviorChanges;
    }

    public void setReportDateTime(LocalDateTime reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public void setReportIsComplete(boolean reportIsComplete) {
        this.reportIsComplete = reportIsComplete;
    }

    public void setOwnerIsOnTrialPeriod(boolean ownerIsOnTrialPeriod) {
        this.ownerIsOnTrialPeriod = ownerIsOnTrialPeriod;
    }
}
