package de.uniba.dsg.cna.qmsurvey;

import de.uniba.dsg.cna.qmsurvey.application.InvalidSessionException;
import de.uniba.dsg.cna.qmsurvey.application.InvalidTokenException;
import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.SurveyService;
import de.uniba.dsg.cna.qmsurvey.application.entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SurveyServiceTests {

    @Autowired
    private SurveyService surveyService;

    @Test
    public void newSurveyIsPersisted() {

        String comment = "test";
        boolean pilot = false;

        Survey survey = surveyService.startNewSurvey(comment , pilot);

        assertNotEquals("", survey.getId());
        assertNotEquals("", survey.getToken());
        assertEquals(comment, survey.getComment());
        assertEquals(pilot, survey.isPilot());
        assertNotNull(survey.getStartTime());

    }

    @Test
    public void addSubmitToSurvey() throws InvalidTokenException {

        String someSessionId = "id123";
        Survey survey = surveyService.startNewSurvey("test", false);
        Submit newSubmit = new Submit(someSessionId, LocalDateTime.now(), LocalDateTime.now(), "welcome");

        Submit savedSubmit = surveyService.initializeSubmit(survey.getToken(), someSessionId, LocalDateTime.now());

        assertNotEquals(null, savedSubmit);
        assertNotNull(savedSubmit.getId());
    }

    @Test
    public void addSubmitToNonExistingSurveyShouldThrowException() {
        Submit newSubmit = new Submit("someSessionId", LocalDateTime.now(), LocalDateTime.now(), "welcome");

        Executable initializeSubmit = () -> surveyService.initializeSubmit("nonValidToken", "someSessionId", LocalDateTime.now());

        assertThrows(InvalidTokenException.class, initializeSubmit, "A non-existing token should lead to an exception");
    }

    @Test
    public void addFactorToSubmit() throws InvalidTokenException, InvalidSessionException {
        String someSessionId = "id123";
        Survey survey = surveyService.startNewSurvey("test", false);
        Submit newSubmit = new Submit(someSessionId, LocalDateTime.now(), LocalDateTime.now(), "welcome");
        Submit savedSubmit = surveyService.initializeSubmit(survey.getToken(), someSessionId, LocalDateTime.now());
        Map<String, Impact> impacts = new HashMap<>();
        impacts.put("reliability", new Impact("reliability", 2));
        impacts.put("maintainability", new Impact("maintainability", -2));
        Factor factor = new Factor("awesomeFramework", "Awesome Framework", impacts, List.of(LocalDateTime.now()));

        Submit updatedSubmit = surveyService.submitFactors(survey.getToken(), someSessionId, List.of(factor));

        assertNotNull(updatedSubmit.getFactors().get("awesomeFramework"));
        assertNotNull(updatedSubmit.getFactors().get("awesomeFramework").getImpacts().get("reliability"));
        assertEquals(2, updatedSubmit.getFactors().get("awesomeFramework").getImpacts().get("reliability").getRating());
        assertEquals(-2, updatedSubmit.getFactors().get("awesomeFramework").getImpacts().get("maintainability").getRating());

    }

    @Test
    public void updateFactorShouldChangeUpdateTimestampOnly() throws InvalidTokenException, InvalidSessionException, InterruptedException {
        String someSessionId = "id123";
        Survey survey = surveyService.startNewSurvey("test", false);
        Submit newSubmit = new Submit(someSessionId, LocalDateTime.now(), LocalDateTime.now(), "welcome");
        Submit savedSubmit = surveyService.initializeSubmit(survey.getToken(), someSessionId, LocalDateTime.now());
        Map<String, Impact> impacts = new HashMap<>();
        impacts.put("reliability", new Impact("reliability", 2));
        impacts.put("maintainability", new Impact("maintainability", -2));
        Factor factor = new Factor("awesomeFramework", "Awesome Framework", impacts, List.of(LocalDateTime.now()));

        Submit firstSubmit = surveyService.submitFactors(survey.getToken(), someSessionId, List.of(factor));

        Thread.sleep(100);

        Submit updatedSubmit = surveyService.submitFactors(survey.getToken(), someSessionId, List.of(factor));

        assertEquals(2, updatedSubmit.getFactors().get("awesomeFramework").getEdits().size());
        assertEquals(firstSubmit.getFactors().get("awesomeFramework").getEdits().get(0), updatedSubmit.getFactors().get("awesomeFramework").getEdits().get(0), "Timestamp for first submit should not have changed");

        LocalDateTime currentFirstSubmit = updatedSubmit.getFactors().get("awesomeFramework").getEdits().get(0);
        LocalDateTime currentUpdated = updatedSubmit.getFactors().get("awesomeFramework").getEdits().get(1);
        assertTrue(currentUpdated.isAfter(currentFirstSubmit), currentUpdated + "should have been after " + currentFirstSubmit);
    }

    @Test
    public void addFactorToNonExistingSurveyShouldThrowException() {
        Map<String, Impact> impacts = new HashMap<>();
        impacts.put("reliability", new Impact("reliability",2));
        impacts.put("maintainability", new Impact("maintainability",-2));
        Factor factor = new Factor("awesomeFramework", "Awesome Framework", impacts, List.of(LocalDateTime.now()));

        Executable submitFactor = () -> surveyService.submitFactors("invalidToken", "someSessionId", List.of(factor));

        assertThrows(InvalidTokenException.class, submitFactor, "A non-existing token should lead to an exception");
    }

    @Test
    public void feedbackToPilotSurveyShouldBeSaved() throws InvalidTokenException, InvalidSessionException {
        String technicalFeedback = "good guidance";
        String lengthFeedback = "way too long";
        String contentFeedback = "did not understand everything";
        Map<String, FactorFeedback> factorFeedback = new HashMap<>();
        FactorFeedback isolatedSecretsFeedback = new FactorFeedback("isolatedSecretsFeedback", "Isolated Secrets Feedback","How would that work?");
        factorFeedback.put("isolatedSecrets", isolatedSecretsFeedback);
        FactorFeedback accessControlManagementConsistencyFeedback = new FactorFeedback("accessControlManagementConsistencyFeedback", "Access Control Management Consistency Feedback","How would that work?");
        factorFeedback.put("accessControlManagementConsistency", accessControlManagementConsistencyFeedback);
        String email = "criticalUser@academics.net";
        PilotFeedback feedback = new PilotFeedback(technicalFeedback, lengthFeedback, contentFeedback, factorFeedback, email);

        String someSessionId = "id123";
        Survey survey = surveyService.startNewSurvey("test", true);
        Submit newSubmit = new Submit(someSessionId, LocalDateTime.now(), LocalDateTime.now(), "welcome");
        Submit savedSubmit = surveyService.initializeSubmit(survey.getToken(), someSessionId, LocalDateTime.now());

        Submit updatedSubmit = surveyService.submitPilotFeedback(survey.getToken(), someSessionId, feedback);

        assertEquals(technicalFeedback, updatedSubmit.getPilotFeedback().getTechnicalFeedback());
        assertEquals(lengthFeedback, updatedSubmit.getPilotFeedback().getLengthFeedback());
        assertEquals(contentFeedback, updatedSubmit.getPilotFeedback().getContentFeedback());
        assertEquals(isolatedSecretsFeedback.getFeedback(), updatedSubmit.getPilotFeedback().getFactorFeedback().get("isolatedSecrets").getFeedback());
        assertEquals(accessControlManagementConsistencyFeedback.getFeedback(), updatedSubmit.getPilotFeedback().getFactorFeedback().get("accessControlManagementConsistency").getFeedback());
        assertEquals(email, updatedSubmit.getPilotFeedback().getEmail());

    }

    @Test
    public void submittingFeedbackShouldNotWorkForNonPilotSurvey() throws InvalidTokenException {
        String technicalFeedback = "good guidance";
        String lengthFeedback = "way too long";
        String contentFeedback = "did not understand everything";
        Map<String, FactorFeedback> factorFeedback = new HashMap<>();
        FactorFeedback isolatedSecretsFeedback = new FactorFeedback("isolatedSecretsFeedback", "Isolated Secrets Feedback","How would that work?");
        factorFeedback.put("isolatedSecrets", isolatedSecretsFeedback);
        FactorFeedback accessControlManagementConsistencyFeedback = new FactorFeedback("accessControlManagementConsistencyFeedback", "Access Control Management Consistency Feedback","How would that work?");
        factorFeedback.put("accessControlManagementConsistency", accessControlManagementConsistencyFeedback);
        String email = "criticalUser@academics.net";
        PilotFeedback feedback = new PilotFeedback(technicalFeedback, lengthFeedback, contentFeedback, factorFeedback, email);

        String someSessionId = "id123";
        Survey survey = surveyService.startNewSurvey("test", false);
        Submit newSubmit = new Submit(someSessionId, LocalDateTime.now(), LocalDateTime.now(), "welcome");
        Submit savedSubmit = surveyService.initializeSubmit(survey.getToken(), someSessionId, LocalDateTime.now());

        Executable tryToSubmitFeedback = () -> surveyService.submitPilotFeedback(survey.getToken(), someSessionId, feedback);

        assertThrows(InvalidTokenException.class, tryToSubmitFeedback, "It should not be possible to submit feedback to a non-pilot survey");

    }
}
