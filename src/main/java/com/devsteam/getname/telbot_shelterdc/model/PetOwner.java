package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
/** Класс "усыновителей" кошек, а также волонтёров, работающих с кошками.
 * При этом у волонтеров поде животного будет пустым. */
@Entity
@Table(name = "pet_owner")
public class PetOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_co")
    private Long idCO;
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
    /** Дата начала испытательного периода для "усыновителя" животного.
      */
    @Column(name = "start")
    private LocalDate start;

/** Поле животного, заполняется волонтером после заключения договора.
 * Правило: На испытательный срок - одно животное в одни руки.
 */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pet_id")
    private Pet pet;

/** Архив ежедневных отчетов "усыновителя" питомца в порядке поступления. */
    @OneToMany(mappedBy = "petOwner", cascade = CascadeType.REMOVE) // было - "cat_owner"
    private List<Report> reportList = new LinkedList<>();  //

// --------------------- Constructors ---------------------------------------------------

    /** Пустой конструктор, что бы Hibernate мог осуществлять манипуляции с классом. */
    public PetOwner() {}

    public PetOwner(Long chatId, String fullName, String phone, String address, StatusOwner statusOwner) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusOwner = statusOwner;
    }
    public PetOwner(Long chatId, String fullName, String phone, String address,
                    StatusOwner statusOwner, LocalDate start, Pet pet) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusOwner = statusOwner;
        this.start = start;
        this.pet = pet;
    }
    public PetOwner(Long idCO, Long chatId, String fullName, String phone, String address,
                    StatusOwner statusOwner) {
        this.idCO = idCO;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusOwner = statusOwner;
        }
    public PetOwner(Long idCO, Long chatId, String fullName, String phone, String address,
                    StatusOwner statusOwner, LocalDate start, Pet pet) {
        this.idCO = idCO;
        this.chatId = chatId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.statusOwner = statusOwner;
        this.start = start;
        this.pet = pet;
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

    public StatusOwner getStatusOwner() {
        return statusOwner;
    }

    public void setStatusOwner(StatusOwner statusOwner) {
        this.statusOwner = statusOwner;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }
}
