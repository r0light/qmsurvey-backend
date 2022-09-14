package de.uniba.dsg.cna.qmsurvey.application;

import de.uniba.dsg.cna.qmsurvey.application.entities.Contact;
import de.uniba.dsg.cna.qmsurvey.application.entities.Submit;

import java.util.List;
import java.util.Optional;

public interface SurveyPort {

    public Survey saveSurvey(Survey survey);

    public void deleteSurvey(Survey survey);

    public List<Survey> loadAllSurveys();

    public Optional<Survey> findSurveyById(String id);

    public Optional<Survey> findSurveyByToken(String token);

    public List<Submit> loadAllSubmitsForSurvey(String surveyId);

    public Optional<Submit> findSubmitById(String id);

    public Optional<Submit> findSubmitBySessionId(String sessionId);

    public Submit saveSubmit(Survey survey, Submit submit);

    public void deleteAllSubmitsForSurvey(String surveyId);

    public Contact saveContact(Survey survey, Contact contact);

    public List<Contact> loadAllContacts();

    public List<Contact> loadAllContactsForSurvey(String surveyId);

}
