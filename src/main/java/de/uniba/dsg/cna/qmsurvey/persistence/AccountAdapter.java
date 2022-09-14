package de.uniba.dsg.cna.qmsurvey.persistence;

import de.uniba.dsg.cna.qmsurvey.application.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountAdapter implements de.uniba.dsg.cna.qmsurvey.application.AccountPort {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> loadAllAccounts() {
        return accountRepository.findAll().stream().map(mongoEntity -> mongoEntity.toDomainObject()).collect(Collectors.toList());
    }

    public Optional<Account> findByEmail(String email) {
        Optional<AccountMongoEntity> found =  accountRepository.findAccountByEmail(email);
        if (found.isPresent()) {
            return Optional.of(found.get().toDomainObject());
        } else {
            return Optional.empty();
        }
    }

    public Account saveAccount(Account newAccount) {
        AccountMongoEntity toSave = AccountMongoEntity.of(newAccount);
        AccountMongoEntity saved = accountRepository.save(toSave);
        return saved.toDomainObject();
    }

}
