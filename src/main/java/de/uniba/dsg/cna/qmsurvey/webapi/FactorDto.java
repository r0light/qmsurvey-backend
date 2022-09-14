package de.uniba.dsg.cna.qmsurvey.webapi;

import de.uniba.dsg.cna.qmsurvey.application.entities.Factor;
import de.uniba.dsg.cna.qmsurvey.application.entities.Impact;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Schema(name = "Factor")
public class FactorDto {

    @NotBlank(message = "A factorKey must be provided.")
    private String factorKey;
    @NotBlank(message = "A factorName must be provided.")
    private String factorName;
    @NotEmpty(message = "At least one impact must be provided.")
    private Map<@NotBlank(message = "A quality aspect must be provided.") String, @Min(value=-2, message = "Rating cannot be lower than -2")
    @Max(value=2, message = "Rating cannot be higer than 2") Integer> impacts;

    public FactorDto() {
    }

    public FactorDto(String factorKey, String factorName, Map<String, Integer> impacts) {
        this.factorKey = factorKey;
        this.factorName = factorName;
        this.impacts = impacts;
    }

    Factor toDomainObject() {
        Map<String, Impact> mappedImpacts = new HashMap<>();
        for(var entry : this.impacts.entrySet()) {
            mappedImpacts.put(entry.getKey(), new Impact(entry.getKey(), entry.getValue()));
        }
        return new Factor(this.factorKey, this.factorName, mappedImpacts);
    }

    static FactorDto of(Factor factor) {
        Map<String, Integer> mappedImpacts = new HashMap<>();
        for(var entry : factor.getImpacts().entrySet()) {
            mappedImpacts.put(entry.getKey(), entry.getValue().getRating());
        }
        return new FactorDto(factor.getFactorKey(), factor.getFactorName(), mappedImpacts);
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

    public Map<String, Integer> getImpacts() {
        return impacts;
    }

    public void setImpacts(Map<String, Integer> impacts) {
        this.impacts = impacts;
    }
}
