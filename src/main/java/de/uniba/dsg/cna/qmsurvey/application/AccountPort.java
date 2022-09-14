package de.uniba.dsg.cna.qmsurvey.application;

import java.util.List;
import java.util.Optional;

public interface AccountPort {

    public List<Account> loadAllAccounts();

    public Optional<Account> findByEmail(String email);

    public Account saveAccount(Account newAccount);
}
