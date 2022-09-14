package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Demographics {

    @NotNull
    private String jobArea;
    @NotNull
    private String jobTitle;
    @NotNull
    private String companySector;
    @Min(value=0, message = "The number of years of experience must be zero or greater")
    private int generalExperience;
    @Min(value=0, message = "The number of years of experience must be zero or greater")
    private int cloudExperience;

    public Demographics() {
        this.jobArea = "";
        this.jobTitle = "";
        this.companySector = "";
        this.generalExperience = 0;
        this.cloudExperience = 0;
    }

    public Demographics(String jobArea, String jobTitle, String companySector, int generalExperience, int cloudExperience) {
        this.jobArea = jobArea;
        this.jobTitle = jobTitle;
        this.companySector = companySector;
        this.generalExperience = generalExperience;
        this.cloudExperience = cloudExperience;
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

    @Override
    public String toString() {
        return "Demographics{" +
                "jobArea='" + jobArea + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", companySector='" + companySector + '\'' +
                ", generalExperience=" + generalExperience +
                ", cloudExperience=" + cloudExperience +
                '}';
    }
}
