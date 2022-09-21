package de.uniba.dsg.cna.qmsurvey.webapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.uniba.dsg.cna.qmsurvey.application.JsonRepresentation;
import de.uniba.dsg.cna.qmsurvey.application.Survey;
import de.uniba.dsg.cna.qmsurvey.application.SurveyService;
import de.uniba.dsg.cna.qmsurvey.application.entities.Contact;
import de.uniba.dsg.cna.qmsurvey.application.entities.Submit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("admin")
@EnableWebSecurity
public class SurveyDetailsController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/{id}")
    public String surveyDetails(@PathVariable("id") String surveyId, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, Model model) {

        Optional<Survey> survey = surveyService.loadSurveyById(surveyId);

        if (survey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such survey.");
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Submit> submitsPage = survey.get().getSubmitsPage(PageRequest.of(currentPage - 1, pageSize));

        List<Contact> contacts = surveyService.loadAllContactsForSurvey(survey.get().getId());

        model.addAttribute("survey", SurveyAttributes.of(survey.get()));
        model.addAttribute("submitsPage", submitsPage);
        int totalPages = submitsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("contacts", contacts);

        return "surveyDetails";
    }

    @GetMapping(path = "/{id}/json" , produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> surveyDetailsAsJson(@PathVariable("id") String surveyId, Model model) {

        Optional<Survey> survey = surveyService.loadSurveyById(surveyId);

        if (survey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such survey.");
        }

        try {
            JsonRepresentation asJson = survey.get().toJsonFile();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + asJson.getFileName() + ".json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(asJson.getJsonContent().length)
                    .body(asJson.getJsonContent());
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The JSON data is currently not available");
        }
    }

}
