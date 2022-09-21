package de.uniba.dsg.cna.qmsurvey.application.entities;

import java.util.Map;

public class FlatFactorRating {

    private String surveyId;
    private String sessionId;
    private String factorKey;
    private Map<String, Integer> aspectRatings;

    public FlatFactorRating() {
    }

    public FlatFactorRating(String surveyId, String sessionId, String factorKey, Map<String, Integer> aspectRatings) {
        this.surveyId = surveyId;
        this.sessionId = sessionId;
        this.factorKey = factorKey;
        this.aspectRatings = aspectRatings;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFactorKey() {
        return factorKey;
    }

    public void setFactorKey(String factorKey) {
        this.factorKey = factorKey;
    }

    public Map<String, Integer> getAspectRatings() {
        return aspectRatings;
    }

    public void setAspectRatings(Map<String, Integer> aspectRatings) {
        this.aspectRatings = aspectRatings;
    }

    @Override
    public String toString() {
        return "FlatFactorRating{" +
                "surveyId='" + surveyId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", factorKey='" + factorKey + '\'' +
                ", aspectRatings=" + aspectRatings +
                '}';
    }
}
