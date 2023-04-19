package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.DTO.ShelterDTO;
import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import static com.devsteam.getname.telbot_shelterdc.Utils.stringValidation;


//сервис для работы с базой данных приюта

/**
 * Сервис для работы с БД приютов
 */
@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * сохраняет объект приют в БД
     * @param shelter сущность приют
     */
    public void save(Shelter shelter){
        shelterRepository.save(shelter);
    }

    /**
     * получает объект приют из БД по идентификатору
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter getByID(int id){
        return shelterRepository.findByID(id);
    }

    public Shelter editShelter(ShelterDTO shelterDTO){

        if(shelterDTO.getID()!=0){
           Shelter shelterToEdit = shelterRepository.findByID(shelterDTO.getID());

        if(stringValidation(shelterDTO.getTransportingRules())){
            shelterToEdit.setTransportingRules(shelterDTO.getTransportingRules());
        }
        if(stringValidation(shelterDTO.getMeetAndGreatRules())){
            shelterToEdit.setMeetAndGreatRules(shelterDTO.getMeetAndGreatRules());
        }
        if(stringValidation(shelterDTO.getRecommendations())){
            shelterToEdit.setRecommendations(shelterDTO.getRecommendations());
        }
        if(stringValidation(shelterDTO.getRecommendationsAdult())){
            shelterToEdit.setRecommendationsAdult(shelterDTO.getRecommendationsAdult());
        }
        if(stringValidation(shelterDTO.getRecommendedCynologists())){
            shelterToEdit.setRecommendedCynologists(shelterDTO.getRecommendedCynologists());
        }
        if(stringValidation(shelterDTO.getCynologistAdvice())){
            shelterToEdit.setCynologistAdvice(shelterDTO.getCynologistAdvice());
        }
        if(stringValidation(shelterDTO.getDocList())){
            shelterToEdit.setDocList(shelterDTO.getDocList());
        }
        if(stringValidation(shelterDTO.getRecommendationsDisabled())){
            shelterToEdit.setRecommendationsDisabled(shelterDTO.getRecommendationsDisabled());
        }
            if(stringValidation(shelterDTO.getAddress())){
                shelterToEdit.setAddress(shelterDTO.getAddress());
            }
            if(stringValidation(shelterDTO.getMapLink())){
                shelterToEdit.setMapLink(shelterDTO.getMapLink());
            }
            if(stringValidation(shelterDTO.getTitle())){
                shelterToEdit.setTitle(shelterDTO.getTitle());
            }

            if(stringValidation(shelterDTO.getSchedule())){
                shelterToEdit.setSchedule(shelterDTO.getSchedule());
            }
            if(stringValidation(shelterDTO.getSecurity())){
                shelterToEdit.setSecurity(shelterDTO.getSecurity());
            }
            if(stringValidation(shelterDTO.getSafetyPrecautions())){
                shelterToEdit.setSecurity(shelterDTO.getSecurity());
            }

        return shelterRepository.save(shelterToEdit);}
        return null;
    }


    public Shelter editShelterAdress(int id, String address, String schedule, String security, String title, String mapLink ) {
        Shelter shelterToEdit = getByID(id);
        if(stringValidation(address)){
            shelterToEdit.setAddress(address);
        }
        if(stringValidation(mapLink)){
            shelterToEdit.setMapLink(mapLink);
        }
        if(stringValidation(title)){
            shelterToEdit.setTitle(title);
        }

        if(stringValidation(schedule)){
            shelterToEdit.setSchedule(schedule);
        }
        if(stringValidation(security)){
            shelterToEdit.setSecurity(security);
        }

        return shelterRepository.save(shelterToEdit);
    }

    public Shelter editSafetyRules(int id, String safetyPrescriptions) {
        Shelter shelterToEdit = getByID(id);
        shelterToEdit.setSafetyPrecautions(safetyPrescriptions);
       return shelterRepository.save(shelterToEdit);
    }
}
