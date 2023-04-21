package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "dog_owner")
public class DogOwner {               // Модель базы данных владельцев собак
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_do",nullable = false)
   private Long idDO;
    @Column(name = "chat_id")
   private Long chatId;
    @Column(name = "full_name")
   private String fullName;
    @Column(name = "phone")
   private String phone;
    @Column(name = "address")
   private String address;
    @Column(name = "status")
   private StatusOwner status;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
   private Dog dog;    // Правило приюта: На испытательный срок - одно животное в одни руки.
   @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<DogReport> reportList = new LinkedList<>(); // Архив ежедневных отчетов "усыновителя" питомца.

//-------------------- Constructors ---------------------------------------------------

   public DogOwner() {}

   public DogOwner(String fullName, String phone, String address, StatusOwner status) {
      this.fullName = fullName;
      this.phone = phone;
      this.address = address;
      this.status = status;
   }

   public DogOwner(Long idDO, Long chatId, String fullName, String phone, String address, StatusOwner status) {
      this.idDO = idDO;
      this.chatId = chatId;
      this.fullName = fullName;
      this.phone = phone;
      this.address = address;
      this.status = status;
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

   public StatusOwner getStatus() {
      return status;
   }

   public void setStatus(StatusOwner status) {
      this.status = status;
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