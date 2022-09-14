package de.uniba.dsg.cna.qmsurvey.persistence;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactRepository extends MongoRepository<ContactMongoEntity, ObjectId> {

    List<ContactMongoEntity> findBySurveyId(ObjectId surveyId);
}
