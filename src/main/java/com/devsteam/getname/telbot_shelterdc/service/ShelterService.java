package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.NoSuchShelterException;
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
     *
     * @param shelter сущность приют
     */
    public void save(Shelter shelter) {
        shelterRepository.save(shelter);
    }

    /**
     * получает объект приют из БД по идентификатору
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter getByID(int id) {
        return shelterRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Редакирует контакты приюта, а именно поля адрес, расписание, контакты охраны, название, ссылку на карты
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editShelterAddress(int id, String address, String schedule, String security, String title, String mapLink) {
        Shelter shelterToEdit = getByID(id);
        if (stringValidation(address)) {
            shelterToEdit.setAddress(address);
        }
        if (stringValidation(mapLink)) {
            shelterToEdit.setMapLink(mapLink);
        }
        if (stringValidation(title)) {
            shelterToEdit.setTitle(title);
        }

        if (stringValidation(schedule)) {
            shelterToEdit.setSchedule(schedule);
        }
        if (stringValidation(security)) {
            shelterToEdit.setSecurity(security);
        }

        return shelterRepository.save(shelterToEdit);
    }

    /**
     * измененяет правиля безопасности в приюте
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editSafetyRules(int id, String safetyPrescriptions) {
        Shelter shelterToEdit = getByID(id);
        shelterToEdit.setSafetyPrecautions(safetyPrescriptions);
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет правила транспортировки животных
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editTransportingRules(int id, String transportingRules) {
        Shelter shelterToEdit = shelterRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (stringValidation(transportingRules)) {
            shelterToEdit.setTransportingRules(transportingRules);
        }
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет рекомендации по обустройству дома
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editRecommendations(int id, String recommendations) {
        Shelter shelterToEdit = shelterRepository.findById(id).orElseThrow(NoSuchShelterException::new);
        if (stringValidation(recommendations)) {
            shelterToEdit.setRecommendations(recommendations);
        }
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет правила знакомства с животным
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editMeetAndGreetRules(int id, String meetAndGreetRules) {
        Shelter shelterToEdit = getByID(id);
        if (stringValidation(meetAndGreetRules)) {
            shelterToEdit.setMeetAndGreatRules(meetAndGreetRules);
        }
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет советы кинологов
     * * @return объект приют
     */
    public Shelter editCynologistsAdvice(String cynologistAdvice) {
        Shelter shelterToEdit = getByID(1);
        if (stringValidation(cynologistAdvice)) {
            shelterToEdit.setCynologistAdvice(cynologistAdvice);
        }
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет список документов для усыновления
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editDocList(int id, String docList) {

        Shelter shelterToEdit = getByID(id);
        if (stringValidation(docList)) {
            shelterToEdit.setDocList(docList);
        }
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет список причин для отказа в усыновлении
     *
     * @param id идентификатор приюта
     * @return объект приют
     */
    public Shelter editRejectReasonList(int id, String rejectReasonList) {
        Shelter shelterToEdit = getByID(id);
        if (stringValidation(rejectReasonList)) {
            shelterToEdit.setRejectReasonsList(rejectReasonList);
        }
        return shelterRepository.save(shelterToEdit);

    }

    /**
     * изменяет список кинологов
     *
     * @return объект приют
     */
    public Shelter editCynologistList(String cynologistList) {
        Shelter shelterToEdit = getByID(1);
        if (stringValidation(cynologistList)) {
            shelterToEdit.setRecommendedCynologists(cynologistList);
        }
        return shelterRepository.save(shelterToEdit);
    }

    /**
     * изменяет описание приюта
     *
     * @return объект приют
     */
    public Shelter editDescription(int id, String about) {
        Shelter shelterToEdit =  getByID(id);
        if (stringValidation(about)) {
            shelterToEdit.setInfo(about);
        }
        return shelterRepository.save(shelterToEdit);


    }
    /**
     * получает всю информацию о приюте
     *
     * @return объект приют
     */
    public Shelter getShelterInfo(int id) {
        return getByID(id);
    }

    /**
     * изменяет рекомендации для усыновителей взрослых животных
     *
     * @return объект приют
     */
    public Shelter editRecommendationsAdult(int id, String recommendationAdults) {
        Shelter shelterToEdit = getByID(id);
        if (stringValidation(recommendationAdults)) {
            shelterToEdit.setRecommendationsAdult(recommendationAdults);
        }
        return shelterRepository.save(shelterToEdit);
    }
    /**
     * изменяет для усыновителей животных-инвалидов
     *
     * @return объект приют
     */
    public Shelter editRecommendationsDisabled(int id, String recommendationDisabled) {
        Shelter shelterToEdit = getByID(id);
        if (stringValidation(recommendationDisabled)) {
            shelterToEdit.setRecommendationsDisabled(recommendationDisabled);
        }
        return shelterRepository.save(shelterToEdit);
    }
}
