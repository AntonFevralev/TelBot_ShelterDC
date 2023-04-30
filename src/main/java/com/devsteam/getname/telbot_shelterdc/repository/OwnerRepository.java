package com.devsteam.getname.telbot_shelterdc.repository;

import com.devsteam.getname.telbot_shelterdc.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository <PetOwner, Long>{
    PetOwner findPetOwnerByChatId(long chatId);
}

