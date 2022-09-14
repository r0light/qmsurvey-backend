package de.uniba.dsg.cna.qmsurvey.webapi;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private final int status;
    private final String message;

    private Map<String, String> details = new HashMap<>();

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(int status, String message, Map<String, String> details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
