package de.uniba.dsg.cna.qmsurvey.webapp;

import de.uniba.dsg.cna.qmsurvey.application.AnalysisService;
import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.SurveyService;
import de.uniba.dsg.cna.qmsurvey.application.entities.FactorResult;
import de.uniba.dsg.cna.qmsurvey.application.entities.FlatDemographics;
import de.uniba.dsg.cna.qmsurvey.application.entities.FlatFactorRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("admin/analysis")
@EnableWebSecurity
public class AnalysisController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/")
    public String analysis(Model model) {

        List<Survey> allSurveys = surveyService.loadAllSurveys();

        List<SurveyAttributes> surveys = allSurveys.stream().map(SurveyAttributes::of).toList();
        model.addAttribute("selectionWrapper", new SurveySelectionWrapper(surveys));
        model.addAttribute("factorResults", List.of());
        model.addAttribute("factorData", List.of());
        model.addAttribute("answerTimeData", List.of());

        return "analysis";
    }

    @PostMapping(path = "/report", params = "action=report")
    public String generateReport(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {

        model.addAttribute("selectionWrapper", selectionWrapper);

        List<FlatFactorRating> flatRatings = getFactorRatingsForSurveys(selectionWrapper.getSurveys());

        // factor rating results
        List<FactorResult> factorResults = analysisService.summarizeFactorResults(flatRatings);
        model.addAttribute("factorResults", factorResults);

        List<FactorChartDataWrapper> factorData = factorResults.stream().map(factorResult -> {
            List<QualityAspectChartData> qualityAspectChartData = new ArrayList<>();
            factorResult.getRatings().entrySet().stream().forEach(entry -> {
                Integer[] frequencies = {0, 0, 0, 0, 0};
                for (int rating : entry.getValue()) {
                    frequencies[rating + 2] = frequencies[rating + 2] + 1;
                }
                qualityAspectChartData.add(new QualityAspectChartData(entry.getKey(), Arrays.asList(frequencies)));
            });
            return new FactorChartDataWrapper(factorResult.getFactorKey(), qualityAspectChartData);
        }).toList();

        model.addAttribute("factorData", factorData);

        // time to answer data
        List<Long> answerTimeData = flatRatings.stream().map(FlatFactorRating::getSecondsToAnswer).toList();
        model.addAttribute("answerTimeData", answerTimeData);

        return "analysis";
    }

    @PostMapping(path = "/report", produces = MediaType.TEXT_PLAIN_VALUE, params = "action=factorsCsv")
    public ResponseEntity<byte[]> exportFactorRatingsAsCsv(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {

        List<FlatFactorRating> flatRatings = getFactorRatingsForSurveys(selectionWrapper.getSurveys());

        List<String> preparedContent = analysisService.convertFactorRatingsToCsv(flatRatings);

        byte[] fileData = String.join("\n", preparedContent).getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "factorData" + ".csv")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(fileData.length)
                .body(fileData);
    }

    private List<FlatFactorRating> getFactorRatingsForSurveys(List<SurveyAttributes> surveys) {
        return analysisService.collectAllFactorRatings(surveys.stream()
                .filter(SurveyAttributes::isSelected)
                .map(SurveyAttributes::getId)
                .toList()
        );
    }

    @PostMapping(path = "/report", produces = MediaType.TEXT_PLAIN_VALUE, params = "action=demographicsCsv")
    public ResponseEntity<byte[]> exportDemographicsAsCsv(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {

        List<FlatDemographics> flatDemographics = analysisService.collectAllDemographics(selectionWrapper.getSurveys().stream().filter(SurveyAttributes::isSelected).map(SurveyAttributes::getId)
                .toList());

        List<String> preparedContent = analysisService.convertDemographicsToCsv(flatDemographics);

        byte[] fileData = String.join("\n", preparedContent).getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "demographicsData" + ".csv")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(fileData.length)
                .body(fileData);
    }

    @PostMapping(path = "/report", produces = MediaType.TEXT_PLAIN_VALUE, params = "action=contactsCsv")
    public ResponseEntity<byte[]> exportContactsAsCsv(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {

        List<String> preparedContent = analysisService.collectContactsAsCsv(selectionWrapper.getSurveys().stream().filter(SurveyAttributes::isSelected).map(SurveyAttributes::getId)
                .toList());

        byte[] fileData = String.join("\n", preparedContent).getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "contacts" + ".csv")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(fileData.length)
                .body(fileData);
    }


}
