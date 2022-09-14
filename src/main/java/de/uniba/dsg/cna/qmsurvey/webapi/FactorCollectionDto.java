package de.uniba.dsg.cna.qmsurvey.webapi;

import de.uniba.dsg.cna.qmsurvey.application.entities.Factor;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "FactorCollection")
public class FactorCollectionDto {

    @NotBlank(message = "A token must be provided.")
    private String token;

    @NotEmpty(message = "At least one factor must be provided.")
    @Valid
    private List<FactorDto> factors;

    public FactorCollectionDto() {
    }

    public FactorCollectionDto(String token, List<FactorDto> factors) {
        this.token = token;
        this.factors = factors;
    }

    static FactorCollectionDto of(String token, List<Factor> factors) {
        return new FactorCollectionDto(token, factors.stream().map(factor -> FactorDto.of(factor)).collect(Collectors.toList()));
    }

    List<Factor> toDomainObjectsCollection() {
        return factors.stream().map(factorDto -> factorDto.toDomainObject()).collect(Collectors.toList());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<FactorDto> getFactors() {
        return factors;
    }

    public void setFactors(List<FactorDto> factors) {
        this.factors = factors;
    }
}
