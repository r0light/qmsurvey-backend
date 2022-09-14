package de.uniba.dsg.cna.qmsurvey.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

interface AccountRepository extends MongoRepository<AccountMongoEntity, String> {

    @Query("{email:'?0'}")
    Optional<AccountMongoEntity> findAccountByEmail(String email);
}
