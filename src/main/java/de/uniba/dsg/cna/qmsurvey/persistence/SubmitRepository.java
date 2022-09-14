package de.uniba.dsg.cna.qmsurvey.persistence;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

interface SubmitRepository extends MongoRepository<SubmitMongoEntity, ObjectId> {

    List<SubmitMongoEntity> findBySurveyId(ObjectId surveyId);

    Optional<SubmitMongoEntity> findSubmitById(ObjectId id);

    @Query("{'sessionId':?0}")
    Optional<SubmitMongoEntity> findSubmitBySessionId(String sessionId);

    @Query(value="{'surveyId':?0}", delete = true)
    public void deleteBySurveyId(ObjectId surveyId);
}
