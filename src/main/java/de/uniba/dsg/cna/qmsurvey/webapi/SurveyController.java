package de.uniba.dsg.cna.qmsurvey.webapi;

import de.uniba.dsg.cna.qmsurvey.application.InvalidClientStatusException;
import de.uniba.dsg.cna.qmsurvey.application.InvalidSessionException;
import de.uniba.dsg.cna.qmsurvey.application.InvalidTokenException;
import de.uniba.dsg.cna.qmsurvey.application.SurveyService;
import de.uniba.dsg.cna.qmsurvey.application.entities.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Operation(summary = "create a new submit with a sessionId for a survey identified by a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The new submit has been saved.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubmitDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data has been provided for the request",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There is currently no survey for this token.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<SubmitDto> initializeSubmit(@RequestBody @Valid SubmitDto newSubmit) {
        try {
            Submit submit = surveyService.initializeSubmit(newSubmit.getToken(), newSubmit.getSessionId(), newSubmit.getClientStartTime());
            boolean isPilot = surveyService.isPilot(newSubmit.getToken());
            return EntityModel.of(SubmitDto.of(newSubmit.getToken(), submit.getSessionId(), submit.getClientStartTime(), submit.getLastState(), isPilot),
                    linkTo(methodOn(SurveyController.class).updateClientStateForSubmit(newSubmit.getSessionId(), new StateUpdateDto())).withRel("updateClientState"),
                    linkTo(methodOn(SurveyController.class).submitFactors(newSubmit.getSessionId(), new FactorCollectionDto())).withRel("submitFactors"));
        } catch (InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no survey for this token.");
        }
    }

    @Operation(summary = "update last client state with a sessionId for a survey identified by a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The new last state has been saved.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubmitDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data has been provided for the request",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There is currently no survey for this token.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PutMapping(value = "{sessionId}/clientstate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<SubmitDto> updateClientStateForSubmit(@PathVariable String sessionId, @RequestBody @Valid StateUpdateDto stateUpdate) {
        try {
            Submit submit = surveyService.updateLastClientState(stateUpdate.getToken(), sessionId, stateUpdate.getLastState());
            boolean isPilot = surveyService.isPilot(stateUpdate.getToken());
            return EntityModel.of(SubmitDto.of(stateUpdate.getToken(), submit.getSessionId(), submit.getClientStartTime(), submit.getLastState(), isPilot),
                    linkTo(methodOn(SurveyController.class).updateClientStateForSubmit(submit.getSessionId(), new StateUpdateDto())).withRel("updateClientState"),
                    linkTo(methodOn(SurveyController.class).submitFactors(submit.getSessionId(), new FactorCollectionDto())).withRel("submitFactors"));
        } catch (InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no survey for this token.");
        } catch (InvalidSessionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this id has been started yet");
        } catch (InvalidClientStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot set this status");
        }
    }

    @Operation(summary = "submit new impacts for a factor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The new impacts have been saved for this factor in this session.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FactorCollectionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data has been provided for the request",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There is currently no survey for this token or no session with this id has been started yet.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "{sessionId}/factors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<FactorCollectionDto> submitFactors(@PathVariable String sessionId, @RequestBody @Valid FactorCollectionDto passedFactors) {
        try {
            Submit updatedSubmit = surveyService.submitFactors(passedFactors.getToken(), sessionId, passedFactors.toDomainObjectsCollection());

            // select only the submitted factors from the currently stated factors
            List<Factor> updatedFactors = passedFactors.getFactors().stream().map(factor -> updatedSubmit.getFactors().get(factor.getFactorKey())).collect(Collectors.toList());

            return EntityModel.of(FactorCollectionDto.of(passedFactors.getToken(), updatedFactors));
        } catch (InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no survey for this token.");
        } catch (InvalidSessionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this id has been started yet");
        }
    }

    @Operation(summary = "submit demographics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The demographics have been saved for this session.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DemographicsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data has been provided for the request",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There is currently no survey for this token or no session with this id has been started yet.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "{sessionId}/demographics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<DemographicsDto> submitDemographics(@PathVariable String sessionId, @RequestBody @Valid DemographicsDto passedDemographics) {
        try {
            Submit updatedSubmit = surveyService.submitDemographics(passedDemographics.getToken(), sessionId, passedDemographics.toDomainObject());
            Demographics savedDemographics = updatedSubmit.getDemographics();
            return EntityModel.of(DemographicsDto.of(savedDemographics, passedDemographics.getToken()));
        } catch (InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no survey for this token.");
        } catch (InvalidSessionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this id has been started yet");
        }
    }

    @Operation(summary = "submit contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The contact has been saved.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data has been provided for the request",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There is currently no survey for this token or no session with this id has been started yet or this is not a pilot study",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "{sessionId}/contact", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<ContactDto> submitContact(@PathVariable String sessionId, @RequestBody @Valid ContactDto passedContact) {
        try {
            Contact savedContact = surveyService.submitContact(passedContact.getToken(), sessionId, passedContact.getEmail());
            return EntityModel.of(ContactDto.of(savedContact, passedContact.getToken()));
        } catch (InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no survey for this token.");
        } catch (InvalidSessionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this id has been started yet");
        }
    }


    @Operation(summary = "submit feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The feedback has been saved for this session.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data has been provided for the request",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "There is currently no survey for this token or no session with this id has been started yet or this is not a pilot survey",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "{sessionId}/feedback", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<FeedbackDto> submitFeedback(@PathVariable String sessionId, @RequestBody @Valid FeedbackDto passedFeedback) {
        try {
            Submit updatedSubmit = surveyService.submitPilotFeedback(passedFeedback.getToken(), sessionId, passedFeedback.toDomainObject());
            PilotFeedback savedFeedback = updatedSubmit.getPilotFeedback();
            return EntityModel.of(FeedbackDto.of(savedFeedback, passedFeedback.getToken()));
        } catch (InvalidTokenException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is currently no survey for this token.");
        } catch (InvalidSessionException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No session with this id has been started yet");
        }
    }

}
