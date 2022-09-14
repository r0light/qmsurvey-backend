package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class PilotFeedback {

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

    public PilotFeedback() {
    }

    public PilotFeedback(String technicalFeedback, String lengthFeedback, String contentFeedback, Map<String, FactorFeedback> factorFeedback, String email) {
        this.technicalFeedback = technicalFeedback;
        this.lengthFeedback = lengthFeedback;
        this.contentFeedback = contentFeedback;
        this.factorFeedback = factorFeedback;
        this.email = email;
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

    @Override
    public String toString() {
        return "PilotFeedback{" +
                "technicalFeedback='" + technicalFeedback + '\'' +
                ", lengthFeedback='" + lengthFeedback + '\'' +
                ", contentFeedback='" + contentFeedback + '\'' +
                ", factorFeedback=" + factorFeedback +
                ", email='" + email + '\'' +
                '}';
    }
}
