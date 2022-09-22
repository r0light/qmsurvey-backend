package de.uniba.dsg.cna.qmsurvey.webapp;

import java.util.List;

public class FactorChartDataWrapper {

    private String factorKey;
    private List<QualityAspectChartData> chartData;

    public FactorChartDataWrapper(String factorKey, List<QualityAspectChartData> chartData) {
        this.factorKey = factorKey;
        this.chartData = chartData;
    }

    public String getFactorKey() {
        return factorKey;
    }

    public void setFactorKey(String factorKey) {
        this.factorKey = factorKey;
    }

    public List<QualityAspectChartData> getChartData() {
        return chartData;
    }

    public void setChartData(List<QualityAspectChartData> chartData) {
        this.chartData = chartData;
    }
}
