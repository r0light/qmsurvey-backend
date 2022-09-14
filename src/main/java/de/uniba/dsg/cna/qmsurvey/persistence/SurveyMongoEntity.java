package de.uniba.dsg.cna.qmsurvey.persistence;

import de.uniba.dsg.cna.qmsurvey.application.Survey;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "surveys")
class SurveyMongoEntity {

    @MongoId
    private ObjectId id;
    private LocalDateTime startTime;
    private String comment;
    private boolean active;
    private String token;

    private boolean pilot;

    SurveyMongoEntity(ObjectId id, LocalDateTime startTime, String comment, boolean active, String token, boolean pilot) {
        this.id = id;
        this.startTime = startTime;
        this.comment = comment;
        this.active = active;
        this.token = token;
        this.pilot = pilot;
    }

    static SurveyMongoEntity of(Survey survey) {
        ObjectId surveyId = survey.getId() == null ? new ObjectId() : new ObjectId(survey.getId());
        return new SurveyMongoEntity(surveyId, survey.getStartTime(), survey.getComment(), survey.isActive(), survey.getToken(), survey.isPilot());
    }

    Survey toDomainObject() {
        return new Survey(id.toHexString(), startTime, comment, active, token, new HashMap<>(), pilot);
    }

    ObjectId getId() {
        return id;
    }

    void setId(ObjectId id) {
        this.id = id;
    }

    LocalDateTime getStartTime() {
        return startTime;
    }

    void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }

    public boolean isPilot() {
        return pilot;
    }

    public void setPilot(boolean pilot) {
        this.pilot = pilot;
    }
}
