package de.uniba.dsg.cna.qmsurvey.webapi;

import de.uniba.dsg.cna.qmsurvey.application.entities.Contact;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "Contact")
public class ContactDto {

    @NotBlank(message = "A token must be provided.")
    private String token;
    @NotBlank(message = "The email must be provided.")
    private String email;

    public ContactDto(String token, String email) {
        this.token = token;
        this.email = email;
    }

    static ContactDto of(Contact contact, String token) {
        return new ContactDto(token, contact.getEmail());
    }

    Contact toDomainObject() {
        return new Contact(email);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
