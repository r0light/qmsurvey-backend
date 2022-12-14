package de.uniba.dsg.cna.qmsurvey.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import de.uniba.dsg.cna.qmsurvey.application.entities.Submit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"submitsChronologically", "getSubmitsPage"})
public class Survey {

    private String id;
    private LocalDateTime startTime;
    private String comment;
    private boolean active;
    private String token;
    private Map<String, Submit> submits;

    private boolean pilot;
    public Survey(LocalDateTime startTime, String comment, String token, boolean pilot) {
        this.startTime = startTime;
        this.comment = comment;
        this.token = token;
        this.pilot = pilot;
        this.active = true;
        this.submits = new HashMap<>();
    }

    public Survey(String id, LocalDateTime startTime, String comment, boolean active, String token, Map<String, Submit> submits, boolean pilot) {
        this.id = id;
        this.startTime = startTime;
        this.comment = comment;
        this.active = active;
        this.token = token;
        this.submits = submits;
        this.pilot = pilot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Submit> getSubmits() {
        return submits;
    }

    public void setSubmits(Map<String, Submit> submits) {
        this.submits = submits;
    }

    public boolean isPilot() {
        return pilot;
    }

    public void setPilot(boolean pilot) {
        this.pilot = pilot;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id='" + id + '\'' +
                ", startTime=" + startTime +
                ", comment='" + comment + '\'' +
                ", active=" + active +
                ", token='" + token + '\'' +
                ", submits=" + submits +
                ", pilot=" + pilot +
                '}';
    }

    public List<Submit> getSubmitsChronologically() {
        // order submits by start time so that the earliest is first
        List<Submit> orderedSubmits = submits.entrySet().stream().map(entry -> entry.getValue()).sorted(Comparator.comparing(Submit::getStartTime)).collect(Collectors.toList());
        return orderedSubmits;
    }

    public Page<Submit> getSubmitsPage(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Submit> allSubmits = getSubmitsChronologically();
        List<Submit> submitsOnPage;

        if (allSubmits.size() < startItem) {
            submitsOnPage = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allSubmits.size());
            submitsOnPage = allSubmits.subList(startItem, toIndex);
        }

        Page<Submit> submitsPage
                = new PageImpl<Submit>(submitsOnPage, PageRequest.of(currentPage, pageSize), allSubmits.size());

        return submitsPage;
    }

    public int countSubmits() {
        return submits.entrySet().size();
    }

    public int countFilledSubmits() {
        return (int) submits.entrySet().stream().filter(submit -> !submit.getValue().getFactors().isEmpty()).count();
    }

    public JsonRepresentation toJsonFile() throws JsonProcessingException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        String surveyAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        byte[] surveyJsonBytes = surveyAsString.getBytes();
        return new JsonRepresentation(token, surveyJsonBytes);
    }
}
