package de.uniba.dsg.cna.qmsurvey.webapi;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(name = "StateUpdate")
public class StateUpdateDto {

    @NotBlank(message = "A token must be provided.")
    private String token;

    @NotNull(message = "A new last state must be provided.")
    private String lastState;

    public StateUpdateDto() {
    }

    public StateUpdateDto(String token, String lastState) {
        this.token = token;
        this.lastState = lastState;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastState() {
        return lastState;
    }

    public void setLastState(String lastState) {
        this.lastState = lastState;
    }
}
