package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "cat_owner")
public class CatOwner {             // Модель базы данных владельцев кошек
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_co",nullable = false)
    private Long idCO;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOwner status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    private Cat cat;    // На испытательный срок - одно животное в одни руки.

  @OneToMany(mappedBy = "catOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatReport> reportList = new LinkedList<>();;  // Архив ежедневных отчетов "усыновителя" питомца.

// --------------------- Constructors ---------------------------------------------------
    public CatOwner() {}

    public CatOwner(String fullName, String phone, String address, StatusOwner status) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public CatOwner(Long idDO, Long chatId, String fullName, String phone, String address, StatusOwner status) {
        this.idCO = idDO;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }
//------------ Getters & setters -------------------------------------------------------

    public Long getIdCO() {
        return idCO;
    }

    public void setIdCO(Long idCO) {
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

    public StatusOwner getStatus() {
        return status;
    }

    public void setStatus(StatusOwner status) {
        this.status = status;
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
