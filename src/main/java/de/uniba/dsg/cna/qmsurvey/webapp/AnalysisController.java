package de.uniba.dsg.cna.qmsurvey.webapp;

import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/analysis")
@EnableWebSecurity
public class AnalysisController {

        @Autowired
        private SurveyService surveyService;

        @GetMapping("/")
        public String analysis(Model model) {

            List<Survey> allSurveys = surveyService.loadAllSurveys();

            List<SurveyAttributes> surveys = allSurveys.stream().map(survey -> SurveyAttributes.of(survey)).collect(Collectors.toList());
            model.addAttribute("selectionWrapper", new SurveySelectionWrapper(surveys));

            return "analysis";
        }

        @PostMapping("/report")
        public String generateReport(Model model, @ModelAttribute("selectionWrapper") SurveySelectionWrapper selectionWrapper) {
        System.out.println(selectionWrapper.getSurveys());

        model.addAttribute("selectionWrapper", selectionWrapper);

        return "analysis";
    }

}
