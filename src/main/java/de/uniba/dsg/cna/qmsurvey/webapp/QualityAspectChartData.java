package de.uniba.dsg.cna.qmsurvey.webapp;

import java.util.ArrayList;
import java.util.List;

public class QualityAspectChartData {

    private String aspectKey;
    private List<Integer> labels = List.of(-2, -1, 0, 1, 2);
    private List<Integer> frequencies = new ArrayList<>();

    public QualityAspectChartData(String aspectKey, List<Integer> frequencies) {
        if (frequencies.size() != 5) {
            throw new IllegalArgumentException("Exactly five values need to be provided for the frequencies");
        }
        this.aspectKey = aspectKey;
        this.frequencies = frequencies;
    }

    public String getAspectKey() {
        return aspectKey;
    }

    public void setAspectKey(String aspectKey) {
        this.aspectKey = aspectKey;
    }

    public List<Integer> getLabels() {
        return labels;
    }

    public void setLabels(List<Integer> labels) {
        this.labels = labels;
    }

    public List<Integer> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(List<Integer> frequencies) {
        this.frequencies = frequencies;
    }
}
