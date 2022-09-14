package de.uniba.dsg.cna.qmsurvey.application;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class Account {

    private String userId;
    @NotBlank
    @Email(message = "A valid email address is required.")
    private String email;
    @NotBlank
    private String password;

    public Account(String email) {
        this.email = email;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(String userId, String email, String password) {
        this(email, password);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return userId == account.userId && email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @AssertTrue(message = "Please provide a password of at least 8 characters.")
    public boolean isPasswordLongEnough() {
        return password.length() >= 8;
    }
}
