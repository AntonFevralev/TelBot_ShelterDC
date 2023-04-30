package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "pet_reports")
/**
 * Класс отчёта присылаемого в приют владельцем взятого из этого приюта на испытательном сроке
 * @author Черемисин Руслан
 * */
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**Поле id*/
    private long id;
    @ManyToOne(targetEntity = Pet.class)
    @JoinColumn(name = "pet_id", nullable = false)
    /**Поле животного, о котором пишется отчёт*/
    private Pet pet;
    @ManyToOne(targetEntity = PetOwner.class)
    @JoinColumn(name = "pet_owner_id", nullable = false)
    /**Поле владельца животного, который пишет отчёт*/
    private PetOwner petOwner;
    @Column
    /**Поле содержащее ссылку на фото животного, которое прилагается к отчёту*/
    private String photo;
    @Column
    /**Поле с описанием рациона и режима питания животного*/
    private String meals;
    @Column()
    /**Поле с описанием адаптации и состояния животного*/
    private String wellBeingAndAdaptation;
    @Column()
    /**Поле с описанием изменений в поведении животного*/
    private String behaviorChanges;
    @Column()
    /**Поле с датой отправки отчёта*/
    private LocalDate reportDate;
    @Column()
    /**Поле со временем отправки отчёта*/
    private LocalTime reportTime;

    @Column
    /**Поле, отмечающее завершённость(полноценность) отчёта*/
    private boolean reportIsComplete;
    /**Поле, указывающее на факт просмотра отчёта волонтёром*/
    private boolean reportIsInspected;

    public Report() {
    }

    public long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public PetOwner getPetOwner() {
        return petOwner;
    }

    public String getPhoto() {
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


    public boolean isReportIsComplete() {
        return reportIsComplete;
    }

    public boolean isReportIsInspected() {
        return reportIsInspected;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setPetOwner(PetOwner petOwner) {
        this.petOwner = petOwner;
    }

    public void setPhoto(String photo) {
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


    public void setReportIsComplete(boolean reportIsComplete) {
        this.reportIsComplete = reportIsComplete;
    }

    public void setReportIsInspected(boolean reportIsInspected) {
        this.reportIsInspected = reportIsInspected;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalTime reportTime) {
        this.reportTime = reportTime;
    }
}
