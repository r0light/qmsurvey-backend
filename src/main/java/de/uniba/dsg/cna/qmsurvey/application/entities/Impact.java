package de.uniba.dsg.cna.qmsurvey.application.entities;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Impact {
    @NotBlank(message = "An aspectKey must be provided.")
    private String aspectKey;

    @Min(value=-2, message = "Rating cannot be lower than -2")
    @Max(value=2, message = "Rating cannot be higer than 2")
    private int rating;

    public Impact(String aspectKey, int rating) {
        this.aspectKey = aspectKey;
        this.rating = rating;
    }

    public String getAspectKey() {
        return aspectKey;
    }

    public void setAspectKey(String aspectKey) {
        this.aspectKey = aspectKey;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Impact{" +
                "aspectKey='" + aspectKey + '\'' +
                ", rating=" + rating +
                '}';
    }
}
