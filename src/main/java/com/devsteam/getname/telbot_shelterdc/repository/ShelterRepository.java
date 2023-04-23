package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//репозиторий посредством которого идет работа сущностью Shelter
public interface ShelterRepository extends JpaRepository<Shelter, Integer> {
Shelter findByID(int id);


}
