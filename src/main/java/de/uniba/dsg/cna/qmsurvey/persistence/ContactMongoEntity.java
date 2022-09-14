package de.uniba.dsg.cna.qmsurvey.persistence;

import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.entities.Contact;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "contacts")
public class ContactMongoEntity {

    @MongoId
    private ObjectId id;

    private String email;

    private ObjectId surveyId;

    public ContactMongoEntity(ObjectId id, String email, ObjectId surveyId) {
        this.id = id;
        this.email = email;
        this.surveyId = surveyId;
    }

    static ContactMongoEntity of(Contact contact, Survey survey) {
        ObjectId contactId = contact.getId() == null ? new ObjectId() : new ObjectId(contact.getId());
        return new ContactMongoEntity(contactId, contact.getEmail(), new ObjectId(survey.getId()));
    }

    Contact toDomainObject() {
        return new Contact(id.toHexString(), email);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
