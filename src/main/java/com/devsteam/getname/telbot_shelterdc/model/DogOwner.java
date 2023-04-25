package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
/** Класс "усыновителей" собак, а также волонтёров и кинологов, работающих с собаками.
 * При этом у волонтеров и кинологов поде животного будет путым. */
@Entity
@Table(name = "dog_owner")
public class DogOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_do")
   private Long idDO;
    @Column(name = "chat_id", nullable = false)
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
    @JoinColumn(name = "dog_id")
   private Dog dog;

/** Архив ежедневных отчетов "усыновителя" питомца в порядке поступления.
    */
   @OneToMany(mappedBy = "dogOwner", cascade = CascadeType.ALL, orphanRemoval = true) // было - "dog_owner"
   private List<DogReport> reportList = new LinkedList<>();

//-------------------- Constructors ---------------------------------------------------

   /** Пустой конструктор, что бы Hibernat мог осуществлять манипуляции с классом. */
   public DogOwner() {}

     public DogOwner(Long chatId, String fullName, String phone, String address,
                   StatusOwner statusOwner) {
      this.chatId = chatId;
      this.fullName = fullName;
      this.phone = phone;
      this.address = address;
      this.statusOwner = statusOwner;

   }

   public DogOwner(Long idDO, Long chatId, String fullName, String phone, String address,
                   StatusOwner statusOwner, Dog dog) {
      this.idDO = idDO;
      this.chatId = chatId;
      this.fullName = fullName;
      this.phone = phone;
      this.address = address;
      this.statusOwner = statusOwner;
      this.dog = dog;
   }

//------------ Getters & setters -------------------------------------------------------

   public Long getIdDO() {
      return idDO;
   }

   public void setIdDO(Long idDO) {
      this.idDO = idDO;
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

   public Dog getDog() {
      return dog;
   }

   public void setDog(Dog dog) {
      this.dog = dog;
   }

 public List<DogReport> getReportList() {
      return reportList;
   }

   public void setReportList(List<DogReport> reportList) {
      this.reportList = reportList;
   }
}