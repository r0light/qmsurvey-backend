package de.uniba.dsg.cna.qmsurvey.webapp;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@EnableWebSecurity
public class HomeController {

    @GetMapping("/admin")
    public ModelAndView home(Model model) {
        return new ModelAndView("redirect:/admin/");
    }

}
