package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
