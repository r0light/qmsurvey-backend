package de.uniba.dsg.cna.qmsurvey.persistence;

import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.entities.Demographics;
import de.uniba.dsg.cna.qmsurvey.application.entities.Factor;
import de.uniba.dsg.cna.qmsurvey.application.entities.PilotFeedback;
import de.uniba.dsg.cna.qmsurvey.application.entities.Submit;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "submits")
public class SubmitMongoEntity {

    @MongoId
    private ObjectId id;
    private String sessionId;
    private ObjectId surveyId;
    private LocalDateTime startTime;
    private LocalDateTime clientStartTime;

    private String lastState;
    private Map<String, Factor> factors;
    private Demographics demographics;

    private PilotFeedback pilotFeedback;

    public SubmitMongoEntity(ObjectId id, String sessionId, ObjectId surveyId, LocalDateTime startTime, LocalDateTime clientStartTime, String lastState,  Map<String, Factor> factors, Demographics demographics, PilotFeedback pilotFeedback) {
        this.id = id;
        this.sessionId = sessionId;
        this.surveyId = surveyId;
        this.startTime = startTime;
        this.clientStartTime = clientStartTime;
        this.lastState = lastState;
        this.factors = factors;
        this.demographics = demographics;
        this.pilotFeedback = pilotFeedback;
    }

    static SubmitMongoEntity of(Submit submit, Survey survey) {
        ObjectId submitId = submit.getId() == null ? new ObjectId() : new ObjectId(submit.getId());
        return new SubmitMongoEntity(submitId, submit.getSessionId(), new ObjectId(survey.getId()),  submit.getStartTime(), submit.getClientStartTime(), submit.getLastState(), submit.getFactors(), submit.getDemographics(), submit.getPilotFeedback());
    }

    Submit toDomainObject() {
        if (lastState == null || lastState.isEmpty()) {
            lastState = "unknown";
        }
        return new Submit(id.toHexString(), sessionId, startTime, clientStartTime, lastState, factors, demographics ,pilotFeedback);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public ObjectId getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(ObjectId surveyId) {
        this.surveyId = surveyId;
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
}
