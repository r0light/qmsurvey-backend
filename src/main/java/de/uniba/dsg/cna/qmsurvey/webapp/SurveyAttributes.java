package de.uniba.dsg.cna.qmsurvey.webapp;

import de.uniba.dsg.cna.qmsurvey.application.Survey;

import java.time.LocalDateTime;

public class SurveyAttributes {

    private String id;
    private LocalDateTime startTime;
    private String comment;
    private boolean active;
    private String token;
    private boolean pilot;

    private int noOfSubmits;

    private int noOfFilledSubmits;

    private boolean selected;

    public SurveyAttributes() {
    }

    public SurveyAttributes(String id, LocalDateTime startTime, String comment, boolean active, String token, boolean pilot, int noOfSubmits, int noOfFilledSubmits, boolean selected) {
        this.id = id;
        this.startTime = startTime;
        this.comment = comment;
        this.active = active;
        this.token = token;
        this.pilot = pilot;
        this.noOfSubmits = noOfSubmits;
        this.noOfFilledSubmits = noOfFilledSubmits;
        this.selected = selected;
    }

    public static SurveyAttributes of(Survey survey) {
        return new SurveyAttributes(survey.getId(), survey.getStartTime(), survey.getComment(), survey.isActive(), survey.getToken(), survey.isPilot(), survey.countSubmits(), survey.countFilledSubmits(), false);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isPilot() {
        return pilot;
    }

    public void setPilot(boolean pilot) {
        this.pilot = pilot;
    }

    public int getNoOfSubmits() {
        return noOfSubmits;
    }

    public void setNoOfSubmits(int noOfSubmits) {
        this.noOfSubmits = noOfSubmits;
    }

    public int getNoOfFilledSubmits() {
        return noOfFilledSubmits;
    }

    public void setNoOfFilledSubmits(int noOfFilledSubmits) {
        this.noOfFilledSubmits = noOfFilledSubmits;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SurveyAttributes{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", comment='" + comment + '\'' +
                ", active=" + active +
                ", token='" + token + '\'' +
                ", pilot=" + pilot +
                ", noOfSubmits=" + noOfSubmits +
                ", noOfFilledSubmits=" + noOfFilledSubmits +
                ", selected=" + selected +
                '}';
    }
}
