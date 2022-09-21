package de.uniba.dsg.cna.qmsurvey.webapp;

import de.uniba.dsg.cna.qmsurvey.application.Survey;

import java.util.List;

public class SurveySelectionWrapper {

    private List<SurveyAttributes> surveys;

    public SurveySelectionWrapper() {
    }

    public SurveySelectionWrapper(List<SurveyAttributes> surveys) {
        this.surveys = surveys;
    }

    public List<SurveyAttributes> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<SurveyAttributes> surveys) {
        this.surveys = surveys;
    }
}
