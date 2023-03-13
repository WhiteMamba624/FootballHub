package com.gligamihai.footballhub.models;

public class Event {

    private String ownerId;
    private String recommendedPlayerExperienceLevel;
    private String matchDayTitle;
    private String eventDate;
    private String eventStartTime;
    private int eventLength;
    private String eventLocation;
    private int numberOfTeams;
    private int numberOfPlayersPerTeam;
    private int eventCost;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRecommendedPlayerExperienceLevel() {
        return recommendedPlayerExperienceLevel;
    }

    public void setRecommendedPlayerExperienceLevel(String recommendedPlayerExperienceLevel) {
        this.recommendedPlayerExperienceLevel = recommendedPlayerExperienceLevel;
    }

    public String getMatchDayTitle() {
        return matchDayTitle;
    }

    public void setMatchDayTitle(String matchDayTitle) {
        this.matchDayTitle = matchDayTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public int getEventLength() {
        return eventLength;
    }

    public void setEventLength(int eventLength) {
        this.eventLength = eventLength;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public int getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(int numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public int getNumberOfPlayersPerTeam() {
        return numberOfPlayersPerTeam;
    }

    public void setNumberOfPlayersPerTeam(int numberOfPlayersPerTeam) {
        this.numberOfPlayersPerTeam = numberOfPlayersPerTeam;
    }

    public int getEventCost() {
        return eventCost;
    }

    public void setEventCost(int eventCost) {
        this.eventCost = eventCost;
    }

    public Event(){}

    public Event(String ownerId, String recommendedPlayerExperienceLevel, String matchDayTitle, String eventDate, String eventStartTime, int eventLength, String eventLocation, int numberOfTeams, int numberOfPlayersPerTeam, int eventCost) {
        this.ownerId = ownerId;
        this.recommendedPlayerExperienceLevel = recommendedPlayerExperienceLevel;
        this.matchDayTitle = matchDayTitle;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventLength = eventLength;
        this.eventLocation = eventLocation;
        this.numberOfTeams = numberOfTeams;
        this.numberOfPlayersPerTeam = numberOfPlayersPerTeam;
        this.eventCost = eventCost;
    }
}
