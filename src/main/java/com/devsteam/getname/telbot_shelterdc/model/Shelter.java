package com.devsteam.getname.telbot_shelterdc.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * класс содердит основную информацию о приюте собак
 */

public class Shelter {
    private int ID;
    /**
     * название приюта
     */
    private String title;
    /**
     * расписание приюта
     */
    private String schedule;
    /**
     * адрес
     */
    private String address;
    /**
     * схема проезда в виде ссылки на гугл карты
     */
    private String mapLink;
    /**
     * контактные данные охраны
     */
    private String security;
    /**
     * информация о приюте
     */
    private String info;
    /**
     * рекомендации по технике безопасности в приюте
     */
    private String safetyPrecautions;
    /**
     * правила знакомства с животным
     */
    private String meetAndGreatRules;
    /**
     * список документов, необходимых для взятие собаки из приюта
     */
    private String docList;
    /**
     * рекомендации по транспортировке
     */
    private String transportingRules;
    /**
     * рекомедации по обустройству дома для щенка
     */
    private String recommendations;
    /**
     * рекомедации по обустройству дома для взрослой собаки
     */

    private String recommendationsAdult;
    /**
     * рекомендации по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)
     */

    private String recommendationsDisabled;
    /**
     * советы кинолога по первичному общению с собакой
     */
    private String cynologistAdvice;
    /**
     * рекомендации по проверенным кинологам для дальнейшего обращения к ним
     */

    private String recommendedCynologists;
    /**
     * список причин, почему могут отказать и не дать забрать собаку из приюта
     */

    private String rejectReasonsList;
    /**
     * идентификатор чата с волонтером
     */

    private long chatId;


    public Shelter() {
    }

    public Shelter(int ID, String title, String schedule, String address, String mapLink, String security, String info, String safetyPrecautions, String meetAndGreatRules, String docList, String transportingRules, String recommendations, String recommendationsAdult, String recommendationsDisabled, String cynologistAdvice, String recommendedCynologists, String rejectReasonsList, long chatId) {
        this.ID = ID;
        this.title = title;
        this.schedule = schedule;
        this.address = address;
        this.mapLink = mapLink;
        this.security = security;
        this.info = info;
        this.safetyPrecautions = safetyPrecautions;
        this.meetAndGreatRules = meetAndGreatRules;
        this.docList = docList;
        this.transportingRules = transportingRules;
        this.recommendations = recommendations;
        this.recommendationsAdult = recommendationsAdult;
        this.recommendationsDisabled = recommendationsDisabled;
        this.cynologistAdvice = cynologistAdvice;
        this.recommendedCynologists = recommendedCynologists;
        this.rejectReasonsList = rejectReasonsList;
        this.chatId = chatId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSafetyPrecautions() {
        return safetyPrecautions;
    }

    public void setSafetyPrecautions(String safetyPrecautions) {
        this.safetyPrecautions = safetyPrecautions;
    }

    public String getMeetAndGreatRules() {
        return meetAndGreatRules;
    }

    public void setMeetAndGreatRules(String meetAndGreatRules) {
        this.meetAndGreatRules = meetAndGreatRules;
    }

    public String getDocList() {
        return docList;
    }

    public void setDocList(String docList) {
        this.docList = docList;
    }

    public String getTransportingRules() {
        return transportingRules;
    }

    public void setTransportingRules(String transportingRules) {
        this.transportingRules = transportingRules;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getRecommendationsAdult() {
        return recommendationsAdult;
    }

    public void setRecommendationsAdult(String recommendationsAdult) {
        this.recommendationsAdult = recommendationsAdult;
    }

    public String getRecommendationsDisabled() {
        return recommendationsDisabled;
    }

    public void setRecommendationsDisabled(String recommendationsDisabled) {
        this.recommendationsDisabled = recommendationsDisabled;
    }

    public String getCynologistAdvice() {
        return cynologistAdvice;
    }

    public void setCynologistAdvice(String cynologistAdvice) {
        this.cynologistAdvice = cynologistAdvice;
    }

    public String getRecommendedCynologists() {
        return recommendedCynologists;
    }

    public void setRecommendedCynologists(String recommendedCynologists) {
        this.recommendedCynologists = recommendedCynologists;
    }

    public String getRejectReasonsList() {
        return rejectReasonsList;
    }

    public void setRejectReasonsList(String rejectReasonsList) {
        this.rejectReasonsList = rejectReasonsList;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}