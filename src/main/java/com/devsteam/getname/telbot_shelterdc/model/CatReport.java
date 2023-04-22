package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cat_reports")
/**
 * Класс отчёта присылаемого в приют владельцем взятого из этого приюта на испытательном сроке
 * @author Черемисин Руслан
 * */
public class CatReport {
    @Id
    @GeneratedValue
    /**Поле id*/
    private long id;
    @ManyToOne(targetEntity = Cat.class, cascade = CascadeType.ALL)
//    @Column(nullable = false)
    /**Поле животного, о котором пишется отчёт*/
    private Cat cat;
    @ManyToOne(targetEntity = CatOwner.class, cascade = CascadeType.ALL)
//    @Column(name = "cat_owner", nullable = false)
    /**Поле владельца животного, который пишет отчёт*/
    private CatOwner catOwner;
    @Column(nullable = false)
    /**Поле содержащее ссылку на фото животного, которое прилагается к отчёту*/
    private String photo;
    @Column(nullable = false)
    /**Поле с описанием рациона и режима питания животного*/
    private String meals;
    @Column(nullable = false)
    /**Поле с описанием адаптации и состояния животного*/
    private String wellBeingAndAdaptation;
    @Column(nullable = false)
    /**Поле с описанием изменений в поведении животного*/
    private String behaviorChanges;
    @Column(nullable = false)
    /**Поле с датой и временем отправки отчёта*/
    private LocalDateTime reportDateTime;

    @Column(nullable = false)
    /**Поле, отмечающее завершённость(полноценность) отчёта*/
    private boolean reportIsComplete;
    /**Поле, указывающее на факт просмотра отчёта волонтёром*/
    private boolean reportIsInspected;

    public CatReport() {
    }

    public long getId() {
        return id;
    }

    public Cat getCat() {
        return cat;
    }

    public CatOwner getCatOwner() {
        return catOwner;
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

    public LocalDateTime getReportDateTime() {
        return reportDateTime;
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

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public void setCatOwner(CatOwner catOwner) {
        this.catOwner = catOwner;
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

    public void setReportDateTime(LocalDateTime reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public void setReportIsComplete(boolean reportIsComplete) {
        this.reportIsComplete = reportIsComplete;
    }

    public void setReportIsInspected(boolean reportIsInspected) {
        this.reportIsInspected = reportIsInspected;
    }
}
