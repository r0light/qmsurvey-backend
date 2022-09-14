package de.uniba.dsg.cna.qmsurvey.webapp;

public class AddSurveyForm {

    private String comment;

    private boolean pilot;

    public AddSurveyForm() {
    }

    public AddSurveyForm(String comment, boolean pilot) {
        this.comment = comment;
        this.pilot = pilot;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isPilot() {
        return pilot;
    }

    public void setPilot(boolean pilot) {
        this.pilot = pilot;
    }
}
