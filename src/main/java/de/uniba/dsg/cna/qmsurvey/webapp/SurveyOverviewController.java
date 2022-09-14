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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("admin")
@EnableWebSecurity
public class SurveyOverviewController {

    @Autowired
    private SurveyService surveyService;

    @ModelAttribute("module")
    String module() {
        return "home";
    }

    @GetMapping("/")
    public String home(Model model) {

        List<Survey> surveys = surveyService.loadAllSurveys();
        model.addAttribute("surveys", surveys);

        AddSurveyForm addSurveyForm = new AddSurveyForm();
        model.addAttribute("addSurveyForm", addSurveyForm);

        return "home";
    }
    @PostMapping("/addSurvey")
    public ModelAndView addSurvey(@ModelAttribute AddSurveyForm addSurveyForm) {
        System.out.println(addSurveyForm.getComment());
        surveyService.startNewSurvey(addSurveyForm.getComment(), addSurveyForm.isPilot());
        return new ModelAndView("redirect:/admin/");
    }

    @RequestMapping(value="/", params={"changeActive"})
    public ModelAndView activateSurvey(final Model model, final HttpServletRequest req) {
        final String surveyId = req.getParameter("changeActive");
        surveyService.changeActive(surveyId);
        return new ModelAndView("redirect:/admin/");
    }

    @RequestMapping(value="/deleteSurvey", params={"delete"})
    public ModelAndView deleteSurvey(final Model model, final HttpServletRequest req) {
        final String surveyId = req.getParameter("delete");
        surveyService.deleteSurvey(surveyId);
        return new ModelAndView("redirect:/admin/");
    }


}
