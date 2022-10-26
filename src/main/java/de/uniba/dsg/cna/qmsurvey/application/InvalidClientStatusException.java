package de.uniba.dsg.cna.qmsurvey.application;

public class InvalidClientStatusException extends Exception {

    public InvalidClientStatusException(String message) {
        super(message);
    }

    public InvalidClientStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClientStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
