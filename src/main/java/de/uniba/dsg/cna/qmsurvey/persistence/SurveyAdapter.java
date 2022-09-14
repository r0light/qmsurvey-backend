package de.uniba.dsg.cna.qmsurvey.persistence;

import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.SurveyPort;
import de.uniba.dsg.cna.qmsurvey.application.entities.Contact;
import de.uniba.dsg.cna.qmsurvey.application.entities.Submit;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SurveyAdapter implements SurveyPort {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SubmitRepository submitRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Survey saveSurvey(Survey survey) {
        SurveyMongoEntity toSave = SurveyMongoEntity.of(survey);
        SurveyMongoEntity saved = surveyRepository.save(toSave);
        return saved.toDomainObject();
    }

    @Override
    public void deleteSurvey(Survey survey) {
        surveyRepository.delete(SurveyMongoEntity.of(survey));
    }

    @Override
    public List<Survey> loadAllSurveys() {
        return surveyRepository.findAll().stream().map(survey -> survey.toDomainObject()).collect(Collectors.toList());
    }

    @Override
    public Optional<Survey> findSurveyById(String id) {
        if (!ObjectId.isValid(id)) {
            return Optional.empty();
        }

        Optional<SurveyMongoEntity> existingSurvey = surveyRepository.findById(new ObjectId(id));

        if(existingSurvey.isPresent()) {
            return Optional.of(existingSurvey.get().toDomainObject());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Survey> findSurveyByToken(String token) {
        Optional<SurveyMongoEntity> existingSurvey = surveyRepository.findSurveyByToken(token);

        if(existingSurvey.isPresent()) {
            return Optional.of(existingSurvey.get().toDomainObject());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Submit> loadAllSubmitsForSurvey(String surveyId) {
        return submitRepository.findBySurveyId(new ObjectId(surveyId)).stream().map(submit -> submit.toDomainObject()).collect(Collectors.toList());
    }

    @Override
    public Optional<Submit> findSubmitById(String id) {
        Optional<SubmitMongoEntity> existingSubmit = submitRepository.findSubmitById(new ObjectId(id));

        if(existingSubmit.isPresent()) {
            return Optional.of(existingSubmit.get().toDomainObject());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Submit> findSubmitBySessionId(String sessionId) {
        Optional<SubmitMongoEntity> existingSubmit = submitRepository.findSubmitBySessionId(sessionId);

        if(existingSubmit.isPresent()) {
            return Optional.of(existingSubmit.get().toDomainObject());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Submit saveSubmit(Survey survey, Submit submit) {

        SubmitMongoEntity toSave = SubmitMongoEntity.of(submit, survey);

        SubmitMongoEntity saved = submitRepository.save(toSave);
        return saved.toDomainObject();
    }

    @Override
    public void deleteAllSubmitsForSurvey(String surveyId) {
        submitRepository.deleteBySurveyId(new ObjectId(surveyId));
    }

    @Override
    public Contact saveContact(Survey survey, Contact contact) {

        ContactMongoEntity toSave = ContactMongoEntity.of(contact, survey);
        ContactMongoEntity saved = contactRepository.save(toSave);

        return saved.toDomainObject();
    }

    @Override
    public List<Contact> loadAllContacts() {
        return contactRepository.findAll().stream().map(contact -> contact.toDomainObject()).collect(Collectors.toList());
    }

    @Override
    public List<Contact> loadAllContactsForSurvey(String surveyId) {
        return contactRepository.findBySurveyId(new ObjectId(surveyId)).stream().map(contact -> contact.toDomainObject()).collect(Collectors.toList());
    }

    private SurveyMongoEntity findSurvey(String id) {
        Optional<SurveyMongoEntity> foundSurvey = surveyRepository.findById(new ObjectId(id));
        return foundSurvey.get();
    }

}
