package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/shelter")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }
    @PostMapping
    public void editShelter(@RequestParam int ID,
                            @RequestParam(required = false) String address,
                            @RequestParam(required = false)  String mapLink,
                            @RequestParam(required = false) String about,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String schedule,
                            @RequestParam(required = false)  String security,
                            @RequestParam(required = false)  long volunteerChatId,
                            @RequestParam(required = false)  String transportingRules,
                            @RequestParam(required = false)  String meetAndGreetRules,
                            @RequestParam(required = false)  String recommendations,
                            @RequestParam(required = false) String recommendationsAdults,
                            @RequestParam(required = false)  String docList,
                            @RequestParam(required = false)  String cynologistAdvice,
                            @RequestParam(required = false)  String recommendedCynologists,
                            @RequestParam(required = false)  String recommendationDisabled){

        shelterService.editShelter(ID, address, mapLink, about, title, schedule, security, volunteerChatId, transportingRules, meetAndGreetRules, recommendations,recommendationsAdults, docList, cynologistAdvice, recommendedCynologists, recommendationDisabled);

    }
}
