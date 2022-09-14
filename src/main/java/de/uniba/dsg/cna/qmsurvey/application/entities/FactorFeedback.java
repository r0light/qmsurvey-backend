package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FactorFeedback {


    @NotBlank(message = "A factor key must be provided.")
    private String key;

    @NotBlank(message = "A factor name must be provided.")
    private String name;

    @NotNull(message = "A factor feedback must be provided.")
    private String feedback;

    public FactorFeedback(String key, String name, String feedback) {
        this.key = key;
        this.name = name;
        this.feedback = feedback;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "FactorFeedback{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
