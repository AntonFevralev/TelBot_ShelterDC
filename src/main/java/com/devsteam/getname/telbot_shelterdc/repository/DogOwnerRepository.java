package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.Dog;
import com.devsteam.getname.telbot_shelterdc.model.DogOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogOwnerRepository extends JpaRepository<DogOwner, Integer> {
}
