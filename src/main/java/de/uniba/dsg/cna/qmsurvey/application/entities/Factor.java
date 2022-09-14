package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Factor {

    @NotBlank(message = "A factorKey must be provided.")
    private String factorKey;
    @NotBlank(message = "A factorName must be provided.")
    private String factorName;

    @NotEmpty(message = "At least one impact must be provided.")
    private Map<@NotBlank(message = "A quality aspect must be provided.") String, @Valid Impact> impacts;

    @NotNull(message = "List of edits must be initialized.")
    private List<LocalDateTime> edits = new ArrayList<>();

    public Factor() {
        super();
    }

    public Factor(String factorKey, String factorName, Map<String, Impact> impacts, List<LocalDateTime> edits) {
        this.factorKey = factorKey;
        this.factorName = factorName;
        this.impacts = impacts;
        this.edits = edits;
    }

    public Factor(String factorKey, String factorName, Map<String, Impact> impacts) {
        this.factorKey = factorKey;
        this.factorName = factorName;
        this.impacts = impacts;
    }

    public String getFactorKey() {
        return factorKey;
    }

    public void setFactorKey(String factorKey) {
        this.factorKey = factorKey;
    }

    public String getFactorName() {
        return factorName;
    }

    public void setFactorName(String factorName) {
        this.factorName = factorName;
    }

    public Map<String, Impact> getImpacts() {
        return impacts;
    }

    public void setImpacts(Map<String, Impact> impacts) {
        this.impacts = impacts;
    }

    public List<LocalDateTime> getEdits() {
        return edits;
    }

    public void setEdits(List<LocalDateTime> edits) {
        this.edits = edits;
    }

    public void addEdit(LocalDateTime editTimestamp) {
        this.edits.add(editTimestamp);
    }

    @Override
    public String toString() {
        return "Factor{" +
                "factorKey='" + factorKey + '\'' +
                ", factorName='" + factorName + '\'' +
                ", impacts=" + impacts +
                ", edits=" + edits +
                '}';
    }
}
