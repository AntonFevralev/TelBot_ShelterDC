package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Cat {

    @Id
    @GeneratedValue
    private Long id;

    private String birthYear;

    private String name;

    private String breed;

    private String description;

    private Enum color;

    private Enum status;

    private Long ownerId;

    public Cat() {
    }

    public Cat(long id, String birthYear, String name, String breed, String description, Enum color, Enum status, Long ownerId) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.color = color;
        this.status = status;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enum getColor() {
        return color;
    }

    public Enum getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setColor(Enum color) {
        this.color = color;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }
}
