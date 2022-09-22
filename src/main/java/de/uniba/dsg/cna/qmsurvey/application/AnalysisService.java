package de.uniba.dsg.cna.qmsurvey.application;

import de.uniba.dsg.cna.qmsurvey.application.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    public static final List<String> QUALITY_ASPECTS = List.of("confidentiality", "integrity", "nonrepudiation", "accountability", "authenticity", "modularity", "reusability", "analyzability", "modifiability", "testability", "simplicity", "timeBehaviour", "resourceUtilization", "capability", "elasticity", "adaptability", "installability", "replaceability", "availability", "faultTolerance", "recoverability", "maturity", "coExistence", "interoperability");

    @Autowired
    private SurveyService surveyService;

    public List<FlatFactorRating> collectAllFactorRatings(List<String> surveyIds) {
        List<FlatFactorRating> allFactorRatings = new ArrayList<>();

        List<Survey> loadedSurveys = surveyIds.stream().map(surveyId -> surveyService.loadSurveyById(surveyId)).flatMap(Optional::stream).collect(Collectors.toList());

        for (Survey survey : loadedSurveys) {
            for (String sessionId : survey.getSubmits().keySet()) {
                Submit submit = survey.getSubmits().get(sessionId);

                List<Factor> factors = submit.getFactors().entrySet().stream()
                        .map(entry -> entry.getValue())
                        .sorted(Comparator.comparing(factor -> factor.getEdits().get(0))) //sort by first edit time
                        .collect(Collectors.toList());

                LocalDateTime previousEditTime = factors.get(0).getEdits().get(0);

                for (Factor factor : factors) {

                    // calculate time that it took to answer this factor, for the first one it is zero and it can later be neglected
                    long secondsToAnswer = ChronoUnit.SECONDS.between(previousEditTime, factor.getEdits().get(0));
                    previousEditTime = factor.getEdits().get(0);

                    // extract impact ratings
                    Map<String, Integer> impacts = new HashMap<>();
                    for (String aspectKey : factor.getImpacts().keySet()) {
                        impacts.put(aspectKey, factor.getImpacts().get(aspectKey).getRating());
                    }
                    allFactorRatings.add(new FlatFactorRating(survey.getToken(), sessionId, factor.getFactorKey(), impacts, secondsToAnswer));
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

    public List<String> convertToCsvFileLines(List<FlatFactorRating> factorRatings) {

        List<String> result = new ArrayList<>();
        String header = "token;sessionId;factorKey;answerTime;" + String.join(";", QUALITY_ASPECTS);
        result.add(header);

        result.addAll(factorRatings.stream().map(flatFactorRating -> {
            StringBuilder toAdd = new StringBuilder();
            toAdd.append(flatFactorRating.getSurveyToken() + ";");
            toAdd.append(flatFactorRating.getSessionId() + ";");
            toAdd.append(flatFactorRating.getFactorKey() + ";");
            toAdd.append(flatFactorRating.getSecondsToAnswer() + ";");

            for (String qualityAspect : QUALITY_ASPECTS) {
                if (flatFactorRating.getAspectRatings().containsKey(qualityAspect)) {
                    toAdd.append(flatFactorRating.getAspectRatings().get(qualityAspect) + ";");
                } else {
                    toAdd.append("0;");
                }
            }
            // remove last semicolon
            toAdd.deleteCharAt(toAdd.length() - 1);

            return toAdd.toString();
        }).collect(Collectors.toList()));

        return result;
    }

}
