package de.uniba.dsg.cna.qmsurvey.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountPort accountPort;

    @Autowired
    private Environment env;

    @PostConstruct
    void initializeAdmin() {
        String email = env.getProperty("qmsurvey.admin.email");
        String password = env.getProperty("qmsurvey.admin.password");
        if (accountPort.findByEmail(email).isEmpty()) {
            signUp(email, password);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public Account signUp(String email, String password) {

        if (accountPort.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent()) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }

        Account newAccount = new Account(email, passwordEncoder().encode(password));
        return accountPort.saveAccount(newAccount);
    }

    public void signIn(Account user) {
        SecurityContextHolder.getContext().setAuthentication(authenticate(user));
    }

    private Authentication authenticate(Account user) {
        return new UsernamePasswordAuthenticationToken(createUser(user), null, null);
    }

    private User createUser(Account account) {
        return new User(account.getEmail(), account.getPassword(), List.of());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> found = accountPort.findByEmail(email);
        if (found.isPresent()){
            return createUser(found.get());
        } else {
            throw new UsernameNotFoundException("There is not account with email: " + email);
        }
    }

}
