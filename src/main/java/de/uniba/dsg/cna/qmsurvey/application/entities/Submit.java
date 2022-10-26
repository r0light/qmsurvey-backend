package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Submit {

    private String id;
    @NotBlank(message = "A sessionId must be provided.")
    private String sessionId;
    @NotNull(message = "A startTime must be provided.")
    private LocalDateTime startTime;
    @NotNull(message = "A clientStartTime must be provided.")
    private LocalDateTime clientStartTime;

    @NotNull(message = "A state must be provided.")
    private String lastState;

    @NotNull(message = "At least one factor must be provided.")
    private Map< @NotBlank(message = "A factorKey must be provided.") String, @Valid Factor> factors;
    @NotNull
    private Demographics demographics;

    @NotNull
    private PilotFeedback pilotFeedback;

    public Submit() {
    }

    public Submit(String sessionId, LocalDateTime startTime, LocalDateTime clientStartTime, String lastState) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.clientStartTime = clientStartTime;
        this.lastState = lastState;
        this.factors = new HashMap<>();
        this.demographics = new Demographics();
        this.pilotFeedback = new PilotFeedback();
    }

    public Submit(String id, String sessionId, LocalDateTime startTime, LocalDateTime clientStartTime, String lastState, Map<String, Factor> factors, Demographics demographics, PilotFeedback pilotFeedback) {
        this.id = id;
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.clientStartTime = clientStartTime;
        this.lastState = lastState;
        this.factors = factors;
        this.demographics = demographics;
        this.pilotFeedback = pilotFeedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getClientStartTime() {
        return clientStartTime;
    }

    public void setClientStartTime(LocalDateTime clientStartTime) {
        this.clientStartTime = clientStartTime;
    }

    public String getLastState() {
        return lastState;
    }

    public void setLastState(String lastState) {
        this.lastState = lastState;
    }

    public Map<String, Factor> getFactors() {
        return factors;
    }

    public void setFactors(Map<String, Factor> factors) {
        this.factors = factors;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public PilotFeedback getPilotFeedback() {
        return pilotFeedback;
    }

    public void setPilotFeedback(PilotFeedback pilotFeedback) {
        this.pilotFeedback = pilotFeedback;
    }

    @Override
    public String toString() {
        return "Submit{" +
                "id='" + id + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", startTime=" + startTime +
                ", clientStartTime=" + clientStartTime +
                ", lastState='" + lastState + '\'' +
                ", factors=" + factors +
                ", demographics=" + demographics +
                ", pilotFeedback=" + pilotFeedback +
                '}';
    }
}
