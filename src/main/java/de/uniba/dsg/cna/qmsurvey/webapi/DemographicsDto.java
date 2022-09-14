package de.uniba.dsg.cna.qmsurvey.webapi;

import de.uniba.dsg.cna.qmsurvey.application.entities.Demographics;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(name = "Demographics")
public class DemographicsDto {

    @NotBlank(message = "A token must be provided.")
    private String token;

    @NotNull
    private String jobArea;
    @NotNull
    private String jobTitle;
    @NotNull
    private String companySector;
    @NotNull
    private int generalExperience;
    @NotNull
    private int cloudExperience;

    public DemographicsDto() {
    }

    public DemographicsDto(String token, String jobArea, String jobTitle, String companySector, int generalExperience, int cloudExperience) {
        this.token = token;
        this.jobArea = jobArea;
        this.jobTitle = jobTitle;
        this.companySector = companySector;
        this.generalExperience = generalExperience;
        this.cloudExperience = cloudExperience;
    }

    static DemographicsDto of(Demographics demographics, String token) {
        return new DemographicsDto(token, demographics.getJobArea(), demographics.getJobTitle(), demographics.getCompanySector(), demographics.getGeneralExperience(), demographics.getCloudExperience());
    }

    Demographics toDomainObject() {
        return new Demographics(this.jobArea, this.jobTitle, this.companySector, this.generalExperience, this.cloudExperience);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJobArea() {
        return jobArea;
    }

    public void setJobArea(String jobArea) {
        this.jobArea = jobArea;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanySector() {
        return companySector;
    }

    public void setCompanySector(String companySector) {
        this.companySector = companySector;
    }

    public int getGeneralExperience() {
        return generalExperience;
    }

    public void setGeneralExperience(int generalExperience) {
        this.generalExperience = generalExperience;
    }

    public int getCloudExperience() {
        return cloudExperience;
    }

    public void setCloudExperience(int cloudExperience) {
        this.cloudExperience = cloudExperience;
    }
}
