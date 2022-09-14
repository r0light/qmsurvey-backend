package de.uniba.dsg.cna.qmsurvey.webapi;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(name = "Submit")
public class SubmitDto {

    @NotBlank(message = "A token must be provided.")
    private String token;

    @NotBlank(message = "A token must be provided.")
    private String sessionId;

    @NotNull(message = "A clientStartTime must be provided.")
    private LocalDateTime clientStartTime;

    private boolean pilot;

    public SubmitDto(String token, String sessionId, LocalDateTime clientStartTime, boolean isPilot) {
        this.token = token;
        this.sessionId = sessionId;
        this.clientStartTime = clientStartTime;
        this.pilot = isPilot;
    }

    static SubmitDto of(String token, String sessionId, LocalDateTime clientStartTime, boolean isPilot) {
        return new SubmitDto(token, sessionId, clientStartTime, isPilot);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getClientStartTime() {
        return clientStartTime;
    }

    public void setClientStartTime(LocalDateTime clientStartTime) {
        this.clientStartTime = clientStartTime;
    }

    public boolean isPilot() {
        return pilot;
    }

    public void setPilot(boolean pilot) {
        this.pilot = pilot;
    }

}
