package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
/** Класс "усыновителей" кошек, а также волонтёров, работающих с кошками.
 * При этом у волонтеров поде животного будет пустым. */
@Entity
@Table(name = "cat_owner")
public class CatOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_co",nullable = false)
    private int idCO;
    @Column(name = "chat_id",nullable = false)
    private Long chatId;
    @Column(name = "full_name",nullable = false)
    private String fullName;
    @Column(name = "phone",nullable = false)
    private String phone;
    @Column(name = "address",nullable = false)
    private String address;
    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusOwner statusOwner;

/** Поле животного, заполняется волонтером после заключения договора.
 * Правило: На испытательный срок - одно животное в одни руки.
 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

/** Архив ежедневных отчетов "усыновителя" питомца в порядке поступления. */
    @OneToMany(mappedBy = "cat_owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatReport> reportList = new LinkedList<>();;  //

// --------------------- Constructors ---------------------------------------------------

    /** Пустой конструктор, что бы Hibernat мог осуществлять манипуляции с классом. */
    public CatOwner() {}

    public CatOwner(String fullName, String phone, String address, StatusOwner statusOwner) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusOwner = statusOwner;
    }

    public CatOwner(int idCO, Long chatId, String fullName, String phone, String address, StatusOwner statusOwner) {
        this.idCO = idCO;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusOwner = statusOwner;
    }
//------------ Getters & setters -------------------------------------------------------

    public int getIdCO() {
        return idCO;
    }

    public void setIdCO(int idCO) {
        this.idCO = idCO;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StatusOwner getStatusOwner() {
        return statusOwner;
    }

    public void setStatusOwner(StatusOwner statusOwner) {
        this.statusOwner = statusOwner;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public List<CatReport> getReportList() {
        return reportList;
    }

    public void setReportList(List<CatReport> reportList) {
        this.reportList = reportList;
    }
}
