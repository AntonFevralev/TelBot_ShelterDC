package com.devsteam.getname.telbot_shelterdc.model;

import javax.persistence.*;

@Entity
@Table(name = "cats")
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "birth_year")
    private String birthYear;

    @Column(name = "name")
    private String name;

    @Column(name = "breed")
    private String breed;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    private Color color;

    @Column(name = "status")
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDo")
    private CatOwner catOwner;

    public Cat() {
    }

    public Cat(long id, String birthYear, String name, String breed, String description, Color color, Status status, CatOwner catOwner) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.color = color;
        this.status = status;
        this.catOwner = catOwner;
    }

    public Cat(Long id, String birthYear, String name, String breed, String description, Color color, Status status) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.color = color;
        this.status = status;
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

    public Color getColor() {
        return color;
    }

    public Status getStatus() {
        return status;
    }


    public CatOwner getCatOwner() {
        return catOwner;
    }

    public void setCatOwner(CatOwner catOwner) {
        this.catOwner = catOwner;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
