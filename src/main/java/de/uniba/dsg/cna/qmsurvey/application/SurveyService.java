package de.uniba.dsg.cna.qmsurvey.application;

import de.uniba.dsg.cna.qmsurvey.application.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private static final ZoneId EUROPE_TZ = ZoneId.of(ZoneId.SHORT_IDS.get("ECT"));

    @Autowired
    private SurveyPort surveyPort;

    public List<Survey> loadAllSurveys() {
        return surveyPort.loadAllSurveys();
    }

    public Optional<Survey> loadSurveyById(String id) {
        Optional<Survey> existingSurvey = surveyPort.findSurveyById(id);

        if (existingSurvey.isPresent()) {
            Survey survey = existingSurvey.get();
            List<Submit> submits = surveyPort.loadAllSubmitsForSurvey(survey.getId());
            survey.setSubmits(submits.stream().collect(Collectors.toMap(Submit::getSessionId, Function.identity())));
            return Optional.of(survey);
        }

        return existingSurvey;
    }

    public Survey startNewSurvey(String comment, boolean pilot) {
        LocalDateTime startTime = LocalDateTime.now(EUROPE_TZ);
        String token = generateToken();

        // make sure that no surveys with the same token exist
        while(surveyPort.findSurveyByToken(token).isPresent()) {
            token = generateToken();
        }

        Survey newSurvey = new Survey(startTime, comment, token, pilot);
        Survey savedSurvey = surveyPort.saveSurvey(newSurvey);

        return savedSurvey;
    }

    private String generateToken() {
        // props to https://www.baeldung.com/java-random-string
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public void changeActive(String surveyId) {
        Optional<Survey> foundSurvey = surveyPort.findSurveyById(surveyId);
        if (foundSurvey.isPresent()) {
            Survey survey = foundSurvey.get();
            survey.setActive(!survey.isActive());
            surveyPort.saveSurvey(survey);
        }
    }

    public void deleteSurvey(String surveyId) {
        Optional<Survey> foundSurvey = surveyPort.findSurveyById(surveyId);
        if (foundSurvey.isPresent()) {
            surveyPort.deleteAllSubmitsForSurvey(foundSurvey.get().getId());
            surveyPort.deleteSurvey(foundSurvey.get());
        }
    }

    public boolean isPilot(String token) throws InvalidTokenException {
        Survey survey = validateSurvey(token);
        return survey.isPilot();
    }

    public Submit initializeSubmit(String token, String sessionId, LocalDateTime clientStartTime) throws InvalidTokenException {

        Optional<Survey> foundSurvey = surveyPort.findSurveyByToken(token);

        Survey survey = foundSurvey.orElseThrow(() -> new InvalidTokenException("There exists no survey for the token: " + token));
        if (!survey.isActive()) {
            throw new InvalidTokenException("The survey for this token is not active anymore");
        }

        Optional<Submit> foundSubmit = surveyPort.findSubmitBySessionId(sessionId);

        if (foundSubmit.isPresent()) {
            // if a submit with this sesionId already exists, there is no need to save the submit
            return foundSubmit.get();
        } else {
            LocalDateTime submitStart = LocalDateTime.now(EUROPE_TZ);
            Submit initialSubmit = new Submit(sessionId, submitStart, clientStartTime);

            Submit newSubmit = surveyPort.saveSubmit(survey, initialSubmit);

            return newSubmit;
        }
    }

    public Submit submitFactors(String token, String sessionId, List<Factor> factors) throws InvalidSessionException, InvalidTokenException {
        Survey survey = validateSurvey(token);
        Submit submit = validateSubmit(sessionId);

        LocalDateTime now = LocalDateTime.now(EUROPE_TZ);

        for (Factor factor : factors) {
            Optional<Factor> existingFactor = Optional.ofNullable(submit.getFactors().get(factor.getFactorKey()));
            List<LocalDateTime> previousEdits = new ArrayList<>();
            if (existingFactor.isPresent()) {
                previousEdits.addAll(existingFactor.get().getEdits());
            }
            previousEdits.add(now);
            factor.setEdits(previousEdits);

            // create or overwrite new factor
            submit.getFactors().put(factor.getFactorKey(), factor);
        }

        Submit updatedSubmit = surveyPort.saveSubmit(survey, submit);
        return updatedSubmit;
    }

    private Survey validateSurvey(String token) throws InvalidTokenException {
        Optional<Survey> foundSurvey = surveyPort.findSurveyByToken(token);
        Survey survey = foundSurvey.orElseThrow(() -> new InvalidTokenException("There exists no survey for the token: " + token));
        if (!survey.isActive()) {
            throw new InvalidTokenException("The survey for this token is not active anymore");
        }
        return survey;
    }

    private Submit validateSubmit(String sessionId) throws InvalidSessionException {
        Optional<Submit> foundSubmit = surveyPort.findSubmitBySessionId(sessionId);
        Submit submit = foundSubmit.orElseThrow(() -> new InvalidSessionException("There exists no session with id: " + sessionId));
        return submit;
    }

    public Submit submitDemographics(String token, String sessionId, Demographics demographics) throws InvalidTokenException, InvalidSessionException {
        Survey survey = validateSurvey(token);
        Submit submit = validateSubmit(sessionId);
        submit.setDemographics(demographics);

        Submit updatedSubmit = surveyPort.saveSubmit(survey, submit);
        return updatedSubmit;
    }

    public Submit submitPilotFeedback(String token, String sessionId, PilotFeedback pilotFeedback) throws InvalidTokenException, InvalidSessionException {
        Survey survey = validateSurvey(token);
        if (!survey.isPilot()) {
            throw new InvalidTokenException("This survey is not a pilot survey and you cannot submit feedback for it");
        }
        Submit submit = validateSubmit(sessionId);

        submit.setPilotFeedback(pilotFeedback);

        Submit updatedSubmit = surveyPort.saveSubmit(survey, submit);
        return updatedSubmit;
    }

    public Contact submitContact(String token, String sessionId, String email) throws InvalidTokenException, InvalidSessionException {
        Survey survey = validateSurvey(token);

        if (survey.isPilot()) {
            throw new InvalidTokenException("This survey a pilot survey and you cannot submit a contact for it");
        }
        validateSubmit(sessionId);

        Contact newContact = new Contact(email);
        Contact savedContact = surveyPort.saveContact(survey, newContact);
        return savedContact;
    }

    public List<Contact> loadAllContactsForSurvey(String surveyId) {
        return surveyPort.loadAllContactsForSurvey(surveyId);
    }


}
