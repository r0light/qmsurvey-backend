package de.uniba.dsg.cna.qmsurvey.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class SigninController {
    @RequestMapping(value = "/signin")
    public String signin() {
        return "signin";
    }
}
