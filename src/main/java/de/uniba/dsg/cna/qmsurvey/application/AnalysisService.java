package de.uniba.dsg.cna.qmsurvey.application;

import de.uniba.dsg.cna.qmsurvey.application.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AnalysisService {

    public static final List<String> QUALITY_ASPECTS = List.of("confidentiality", "integrity", "nonrepudiation", "accountability", "authenticity", "modularity", "reusability", "analyzability", "modifiability", "testability", "simplicity", "timeBehaviour", "resourceUtilization", "capability", "elasticity", "adaptability", "installability", "replaceability", "availability", "faultTolerance", "recoverability", "maturity", "coExistence", "interoperability");

    @Autowired
    private SurveyService surveyService;

    public List<FlatFactorRating> collectAllFactorRatings(List<String> surveyIds) {
        List<FlatFactorRating> allFactorRatings = new ArrayList<>();

        List<Survey> loadedSurveys = surveyIds.stream().map(surveyId -> surveyService.loadSurveyById(surveyId)).flatMap(Optional::stream).toList();

        for (Survey survey : loadedSurveys) {
            for (String sessionId : survey.getSubmits().keySet()) {
                Submit submit = survey.getSubmits().get(sessionId);

                List<Factor> factors = submit.getFactors().values().stream()
                        .sorted(Comparator.comparing(factor -> factor.getEdits().get(0))).toList();

                if (factors.isEmpty()) {
                    // if no factors have been rated, continue with next submit
                    continue;
                }

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

        List<String> factorKeys = factorRatings.stream().map(FlatFactorRating::getFactorKey).distinct().toList();

        for (String factorKey : factorKeys) {
            Map<String, List<Integer>> aspectRatings = new HashMap<>();

            factorRatings.stream()
                    .filter(factorRating -> factorRating.getFactorKey().equals(factorKey))
                    .map(factorRating -> factorRating.getAspectRatings().entrySet())
                    .flatMap(Collection::stream).forEach(entry -> {
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

    public List<String> convertFactorRatingsToCsv(List<FlatFactorRating> factorRatings) {

        List<String> result = new ArrayList<>();
        String header = "token;sessionId;factorKey;answerTime;" + String.join(";", QUALITY_ASPECTS);
        result.add(header);

        result.addAll(factorRatings.stream().map(flatFactorRating -> {
            StringBuilder toAdd = new StringBuilder();
            toAdd.append(flatFactorRating.getSurveyToken()).append(";");
            toAdd.append(flatFactorRating.getSessionId()).append(";");
            toAdd.append(flatFactorRating.getFactorKey()).append(";");
            toAdd.append(flatFactorRating.getSecondsToAnswer()).append(";");

            for (String qualityAspect : QUALITY_ASPECTS) {
                if (flatFactorRating.getAspectRatings().containsKey(qualityAspect)) {
                    toAdd.append(flatFactorRating.getAspectRatings().get(qualityAspect)).append(";");
                } else {
                    toAdd.append("0;");
                }
            }
            // remove last semicolon
            toAdd.deleteCharAt(toAdd.length() - 1);

            return toAdd.toString();
        }).toList());

        return result;
    }

    public List<FlatDemographics> collectAllDemographics(List<String> surveyIds) {

        List<FlatDemographics> allDemographics = new ArrayList<>();

        List<Survey> loadedSurveys = surveyIds.stream().map(surveyId -> surveyService.loadSurveyById(surveyId)).flatMap(Optional::stream).toList();

        for (Survey survey : loadedSurveys) {

            allDemographics.addAll(survey.getSubmits().values().stream()
                    .map(submit -> new FlatDemographics(
                            survey.getToken(),
                            submit.getSessionId(),
                            submit.getDemographics().getJobArea(),
                            submit.getDemographics().getJobTitle(),
                            submit.getDemographics().getCompanySector(),
                            submit.getDemographics().getGeneralExperience(),
                            submit.getDemographics().getCloudExperience()
                    ))
                    .toList()
            );
        }
        return allDemographics;
    }

    public List<String> convertDemographicsToCsv(List<FlatDemographics> demographics) {

        List<String> result = new ArrayList<>();
        String header = "token;sessionId;jobArea;jobTitle;companySector;generalExperience;cloudExperience";
        result.add(header);

        result.addAll(demographics.stream().map(demographicValues -> demographicValues.getSurveyToken() + ";" +
                demographicValues.getSessionId() + ";" +
                demographicValues.getJobArea() + ";" +
                demographicValues.getJobTitle() + ";" +
                demographicValues.getCompanySector() + ";" +
                demographicValues.getGeneralExperience() + ";" +
                demographicValues.getCloudExperience()).toList());

        return result;

    }

    public List<String> collectContactsAsCsv(List<String> surveyIds) {

        List<String> contactEmails = new ArrayList<>();
        contactEmails.add("email");

        contactEmails.addAll(surveyIds.stream().map(surveyId -> surveyService.loadAllContactsForSurvey(surveyId)).flatMap(Collection::stream).map(Contact::getEmail).toList());

        return contactEmails;

    }


}
