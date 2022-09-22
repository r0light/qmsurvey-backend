package de.uniba.dsg.cna.qmsurvey.webapp;

import de.uniba.dsg.cna.qmsurvey.application.AnalysisService;
import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.SurveyService;
import de.uniba.dsg.cna.qmsurvey.application.entities.FactorResult;
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
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

            List<SurveyAttributes> surveys = allSurveys.stream().map(survey -> SurveyAttributes.of(survey)).collect(Collectors.toList());
            model.addAttribute("selectionWrapper", new SurveySelectionWrapper(surveys));
            model.addAttribute("factorResults", List.of());

            return "analysis";
        }

        @PostMapping(path = "/report", params="action=report")
        public String generateReport(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {

            model.addAttribute("selectionWrapper", selectionWrapper);

            List<FlatFactorRating> flatRatings = getFactorRatingsForSurveys(selectionWrapper.getSurveys());

            List<FactorResult> factorResults = analysisService.summarizeFactorResults(flatRatings);
            model.addAttribute("factorResults", factorResults);

            return "analysis";
        }

        @PostMapping(path = "/report", produces= MediaType.TEXT_PLAIN_VALUE, params="action=factorsCsv")
        public ResponseEntity<byte[]> exportAsCsv(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {

            List<FlatFactorRating> flatRatings = getFactorRatingsForSurveys(selectionWrapper.getSurveys());

            List<String> preparedContent = analysisService.convertToCsvFileLines(flatRatings);

            byte[] fileData = String.join("\n", preparedContent).getBytes(StandardCharsets.UTF_8);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "factorData" + ".csv")
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(fileData.length)
                    .body(fileData);
        }

        private List<FlatFactorRating> getFactorRatingsForSurveys(List<SurveyAttributes> surveys) {
            return analysisService.collectAllFactorRatings(surveys.stream()
                    .filter(survey -> survey.isSelected())
                    .map(surveyAttributes -> surveyAttributes.getId())
                    .collect(Collectors.toList())
            );
        }

}
