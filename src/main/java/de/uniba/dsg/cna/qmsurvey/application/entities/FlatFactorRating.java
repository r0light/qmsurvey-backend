package de.uniba.dsg.cna.qmsurvey.application.entities;

import java.util.Map;

public class FlatFactorRating {

    private String surveyToken;
    private String sessionId;
    private String factorKey;
    private Map<String, Integer> aspectRatings;

    private long secondsToAnswer;

    public FlatFactorRating() {
    }

    public FlatFactorRating(String surveyId, String sessionId, String factorKey, Map<String, Integer> aspectRatings, long secondsToAnswer) {
        this.surveyToken = surveyId;
        this.sessionId = sessionId;
        this.factorKey = factorKey;
        this.aspectRatings = aspectRatings;
        this.secondsToAnswer = secondsToAnswer;
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

    public long getSecondsToAnswer() {
        return secondsToAnswer;
    }

    public void setSecondsToAnswer(long secondsToAnswer) {
        this.secondsToAnswer = secondsToAnswer;
    }

    @Override
    public String toString() {
        return "FlatFactorRating{" +
                "surveyId='" + surveyToken + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", factorKey='" + factorKey + '\'' +
                ", aspectRatings=" + aspectRatings +
                ", secondsToAnswer=" + secondsToAnswer +
                '}';
    }
}
