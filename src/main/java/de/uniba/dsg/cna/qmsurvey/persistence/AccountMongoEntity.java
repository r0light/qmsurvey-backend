package de.uniba.dsg.cna.qmsurvey.persistence;

import de.uniba.dsg.cna.qmsurvey.application.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "accounts")
class AccountMongoEntity {

    private @MongoId ObjectId id;
    private String email;
    private String password;

    AccountMongoEntity(ObjectId id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    static AccountMongoEntity of(Account account) {
        System.out.println("new account: " + account);
        ObjectId accountId = account.getUserId() == null ? new ObjectId() : new ObjectId(account.getUserId());
        return new AccountMongoEntity(accountId, account.getEmail(), account.getPassword());
    }

    Account toDomainObject() {
        return new Account(id.toHexString(), email, password);
    }

    ObjectId getId() {
        return id;
    }

    void setId(ObjectId id) {
        this.id = id;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }
}
