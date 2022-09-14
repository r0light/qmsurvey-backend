package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.constraints.NotNull;

public class Contact {

    private String id;

    @NotNull
    private String email;

    public Contact(String email) {
        this.email = email;
    }

    public Contact(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
