package de.uniba.dsg.cna.qmsurvey.application.entities;

import java.util.List;
import java.util.Map;

public class FactorResult {

    private String factorKey;
    private Map<String, List<Integer>> ratings;

    public FactorResult() {
    }

    public FactorResult(String factorKey, Map<String, List<Integer>> ratings) {
        this.factorKey = factorKey;
        this.ratings = ratings;
    }

    public String getFactorKey() {
        return factorKey;
    }

    public void setFactorKey(String factorKey) {
        this.factorKey = factorKey;
    }

    public Map<String, List<Integer>> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, List<Integer>> ratings) {
        this.ratings = ratings;
    }
}
