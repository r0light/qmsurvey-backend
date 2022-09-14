package de.uniba.dsg.cna.qmsurvey.persistence;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

interface SurveyRepository extends MongoRepository<SurveyMongoEntity, ObjectId> {

    @Query("{'token':?0}")
    Optional<SurveyMongoEntity> findSurveyByToken(String token);

    Optional<SurveyMongoEntity> findSurveyById(ObjectId id);
}
