package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import org.springframework.http.codec.ServerSentEventHttpMessageWriter;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void editShelter(int id, String address, String mapLink, String about, String title, String schedule, String security,
                            long volunteerChatId, String transportingRules, String meetAndGreetRules, String recommendations,
                            String recommendationsAdults, String docList, String cynologistAdvice, String recommendedCynologists, String recomendationDisabled) {
        Shelter shelterToEdit = getByID(id);
            if(!address.isEmpty()){
                shelterToEdit.setAddress(address);
            }
            if(!mapLink.isEmpty()){
                shelterToEdit.setMapLink(mapLink);
            }
            if(!about.isEmpty()){
                shelterToEdit.setInfo(about);
            }
            if(!title.isEmpty()){
                shelterToEdit.setTitle(title);
            }
            if(!schedule.isEmpty()){
                shelterToEdit.setSchedule(schedule);
            }
            if(!security.isEmpty()){
                shelterToEdit.setSecurity(security);
            }
            if(volunteerChatId!=0){
                shelterToEdit.setChatId(volunteerChatId);
            }
            if(!transportingRules.isEmpty()){
                shelterToEdit.setTransportingRules(transportingRules);
            }
            if(!meetAndGreetRules.isEmpty()){
                shelterToEdit.setMeetAndGreatRules(meetAndGreetRules);
            }
            if(!recommendations.isEmpty()){
                shelterToEdit.setRecommendations(recommendations);
            }
            if(!recommendationsAdults.isEmpty()){
                shelterToEdit.setRecommendationsAdult(recommendationsAdults);
            }
            if(!recommendedCynologists.isEmpty()){
                shelterToEdit.setRecommendedCynologists(recommendedCynologists);
            }
            if(!cynologistAdvice.isEmpty()){
                shelterToEdit.setCynologistAdvice(cynologistAdvice);
            }
            if(!docList.isEmpty()){
                shelterToEdit.setDocList(docList);
            }
            if(!recomendationDisabled.isEmpty()){
                shelterToEdit.setRecommendationsDisabled(recomendationDisabled);
            }
            shelterRepository.save(shelterToEdit);
        }



}
