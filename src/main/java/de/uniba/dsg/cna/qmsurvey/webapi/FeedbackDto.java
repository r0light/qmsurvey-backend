package de.uniba.dsg.cna.qmsurvey.webapi;

import de.uniba.dsg.cna.qmsurvey.application.entities.FactorFeedback;
import de.uniba.dsg.cna.qmsurvey.application.entities.PilotFeedback;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Schema(name = "Feedback")
public class FeedbackDto {

    @NotBlank(message = "A token must be provided.")
    private String token;

    @NotNull
    private String technicalFeedback;

    @NotNull
    private String lengthFeedback;

    @NotNull
    private String contentFeedback;

    @NotNull
    private Map<String, @Valid FactorFeedback> factorFeedback = new HashMap<>();

    @NotNull
    private String email;

    public FeedbackDto() {
    }

    public FeedbackDto(String token, String technicalFeedback, String lengthFeedback, String contentFeedback, Map<String, FactorFeedback> factorFeedback, String email) {
        this.token = token;
        this.technicalFeedback = technicalFeedback;
        this.lengthFeedback = lengthFeedback;
        this.contentFeedback = contentFeedback;
        this.factorFeedback = factorFeedback;
        this.email = email;
    }

    static FeedbackDto of(PilotFeedback pilotFeedback, String token) {
        return new FeedbackDto(token, pilotFeedback.getTechnicalFeedback(), pilotFeedback.getLengthFeedback(), pilotFeedback.getContentFeedback(), pilotFeedback.getFactorFeedback(), pilotFeedback.getEmail());
    }

    PilotFeedback toDomainObject() {
        return new PilotFeedback(technicalFeedback, lengthFeedback, contentFeedback, factorFeedback, email);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTechnicalFeedback() {
        return technicalFeedback;
    }

    public void setTechnicalFeedback(String technicalFeedback) {
        this.technicalFeedback = technicalFeedback;
    }

    public String getLengthFeedback() {
        return lengthFeedback;
    }

    public void setLengthFeedback(String lengthFeedback) {
        this.lengthFeedback = lengthFeedback;
    }

    public String getContentFeedback() {
        return contentFeedback;
    }

    public void setContentFeedback(String contentFeedback) {
        this.contentFeedback = contentFeedback;
    }

    public Map<String, FactorFeedback> getFactorFeedback() {
        return factorFeedback;
    }

    public void setFactorFeedback(Map<String, FactorFeedback> factorFeedback) {
        this.factorFeedback = factorFeedback;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
