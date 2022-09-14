package de.uniba.dsg.cna.qmsurvey.webapp;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice(annotations = Controller.class)
public class WebAppErrorHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleException(ResponseStatusException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(exception.getStatus());
        modelAndView.addObject("status", exception.getStatus().value());
        modelAndView.addObject("message", exception.getReason());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleGenericException(RuntimeException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        modelAndView.addObject("status", 500);
        modelAndView.addObject("error", exception.getLocalizedMessage());
        modelAndView.addObject("message", exception.getLocalizedMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
