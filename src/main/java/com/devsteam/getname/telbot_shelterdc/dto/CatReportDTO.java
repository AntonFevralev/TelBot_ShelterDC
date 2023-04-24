package com.devsteam.getname.telbot_shelterdc.dto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Table (name = "CAT_REPORT_DTO")
public final class CatReportDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**Поле id*/
    private long id;
    @JoinColumn(name = "CAT_ID")
    @Column(nullable = false)
    /**Поле животного, о котором пишется отчёт*/
    private long catId;
    @JoinColumn(name = "CAT_OWNER_ID_CO")
    @Column(nullable = false)
    /**Поле владельца животного, который пишет отчёт*/
    private long catOwnerId;
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

    public CatReportDTO(long id,
                        long catId,
                        long catOwnerId,
                        String photo,
                        String meals,
                        String wellBeingAndAdaptation,
                        String behaviorChanges,
                        LocalDateTime reportDateTime,
                        boolean reportIsComplete,
                        boolean reportIsInspected) {
        this.id = id;
        this.catId = catId;
        this.catOwnerId = catOwnerId;
        this.photo = photo;
        this.meals = meals;
        this.wellBeingAndAdaptation = wellBeingAndAdaptation;
        this.behaviorChanges = behaviorChanges;
        this.reportDateTime = reportDateTime;
        this.reportIsComplete = reportIsComplete;
        this.reportIsInspected = reportIsInspected;
    }

    public CatReportDTO() {

    }

    public void setReportAsComplete() {
        reportIsComplete = true;
    }
    public void setReportAsIncomplete() {
        reportIsComplete = false;
    }
    public void setReportAsInspected() {
        reportIsInspected = true;
    }

    public long id() {
        return id;
    }

    public long catId() {
        return catId;
    }

    public long catOwnerId() {
        return catOwnerId;
    }

    public String photo() {
        return photo;
    }

    public String meals() {
        return meals;
    }

    public String wellBeingAndAdaptation() {
        return wellBeingAndAdaptation;
    }

    public LocalDateTime reportDateTime() {
        return reportDateTime;
    }

    public boolean reportIsComplete() {
        return reportIsComplete;
    }

    public boolean reportIsInspected() {
        return reportIsInspected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CatReportDTO) obj;
        return this.id == that.id &&
                this.catId == that.catId &&
                this.catOwnerId == that.catOwnerId &&
                Objects.equals(this.photo, that.photo) &&
                Objects.equals(this.meals, that.meals) &&
                Objects.equals(this.wellBeingAndAdaptation, that.wellBeingAndAdaptation) &&
                Objects.equals(this.reportDateTime, that.reportDateTime) &&
                this.reportIsComplete == that.reportIsComplete &&
                this.reportIsInspected == that.reportIsInspected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, catId, catOwnerId, photo, meals, wellBeingAndAdaptation, reportDateTime, reportIsComplete, reportIsInspected);
    }

    @Override
    public String toString() {
        return "CatReportDTO[" +
                "id=" + id + ", " +
                "catId=" + catId + ", " +
                "catOwnerId=" + catOwnerId + ", " +
                "photo=" + photo + ", " +
                "meals=" + meals + ", " +
                "wellBeingAndAdaptation=" + wellBeingAndAdaptation + ", " +
                "reportDateTime=" + reportDateTime + ", " +
                "reportIsComplete=" + reportIsComplete + ", " +
                "reportIsInspected=" + reportIsInspected + ']';
    }

}
