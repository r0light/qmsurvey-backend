package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FlatDemographics {

    private String surveyToken;
    private String sessionId;
    private String jobArea;
    private String jobTitle;
    private String companySector;
    private int generalExperience;
    private int cloudExperience;

    public FlatDemographics(String surveyToken, String sessionId, String jobArea, String jobTitle, String companySector, int generalExperience, int cloudExperience) {
        this.surveyToken = surveyToken;
        this.sessionId = sessionId;
        this.jobArea = jobArea;
        this.jobTitle = jobTitle;
        this.companySector = companySector;
        this.generalExperience = generalExperience;
        this.cloudExperience = cloudExperience;
    }

    public FlatDemographics() {
    }

    public String getSurveyToken() {
        return surveyToken;
    }

    public void setSurveyToken(String surveyToken) {
        this.surveyToken = surveyToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getJobArea() {
        return jobArea;
    }

    public void setJobArea(String jobArea) {
        this.jobArea = jobArea;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanySector() {
        return companySector;
    }

    public void setCompanySector(String companySector) {
        this.companySector = companySector;
    }

    public int getGeneralExperience() {
        return generalExperience;
    }

    public void setGeneralExperience(int generalExperience) {
        this.generalExperience = generalExperience;
    }

    public int getCloudExperience() {
        return cloudExperience;
    }

    public void setCloudExperience(int cloudExperience) {
        this.cloudExperience = cloudExperience;
    }

    @Override
    public String toString() {
        return "FlatDemographics{" +
                "surveyToken='" + surveyToken + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", jobArea='" + jobArea + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", companySector='" + companySector + '\'' +
                ", generalExperience=" + generalExperience +
                ", cloudExperience=" + cloudExperience +
                '}';
    }
}
