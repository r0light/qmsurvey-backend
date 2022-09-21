package de.uniba.dsg.cna.qmsurvey.application;

import de.uniba.dsg.cna.qmsurvey.application.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    @Autowired
    private SurveyService surveyService;

    public List<FlatFactorRating> collectAllFactorRatings(List<String> surveyIds) {
        List<FlatFactorRating> allFactorRatings = new ArrayList<>();

        for (String surveyId: surveyIds) {
            Optional<Survey> foundSurvey = surveyService.loadSurveyById(surveyId);
            if (foundSurvey.isPresent()) {
                Survey survey = foundSurvey.get();
                for (String sessionId : survey.getSubmits().keySet()) {
                    Submit submit = survey.getSubmits().get(sessionId);
                    for (String factorKey : submit.getFactors().keySet()) {
                        Factor factor = submit.getFactors().get(factorKey);

                        Map<String, Integer> impacts = new HashMap<>();
                        for (String aspectKey: factor.getImpacts().keySet()) {
                            impacts.put(aspectKey, factor.getImpacts().get(aspectKey).getRating());
                        }
                        allFactorRatings.add(new FlatFactorRating(surveyId, sessionId, factorKey, impacts));
                    }
                }
            }
        }
        return allFactorRatings;
    }

    public List<FactorResult> summarizeFactorResults(List<FlatFactorRating> factorRatings) {
        List<FactorResult> factorResults = new ArrayList<>();

        List<String> factorkeys = factorRatings.stream().map(factorRating -> factorRating.getFactorKey()).distinct().collect(Collectors.toList());

        for (String factorKey : factorkeys) {
            Map<String, List<Integer>> aspectRatings = new HashMap<>();

            factorRatings.stream()
                    .filter(factorRating -> factorRating.getFactorKey().equals(factorKey))
                    .map(factorRating -> factorRating.getAspectRatings().entrySet())
                    .flatMap(entrySet -> entrySet.stream()).forEach(entry -> {
                       if (aspectRatings.containsKey(entry.getKey())) {
                           aspectRatings.get(entry.getKey()).add(entry.getValue());
                       } else {
                           List<Integer> newList = new ArrayList<>();
                           newList.add(entry.getValue());
                           aspectRatings.put(entry.getKey(), newList);
                       }
                    });
            factorResults.add(new FactorResult(factorKey, aspectRatings));
        }

        return factorResults;
    }

}
