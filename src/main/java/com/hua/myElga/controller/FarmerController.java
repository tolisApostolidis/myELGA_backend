package com.hua.myElga.controller;


import com.hua.myElga.payload.UserDetailsImpl;
import com.hua.myElga.payload.request.FarmerProfileRequest;
import com.hua.myElga.payload.response.BreedersResponse;
import com.hua.myElga.payload.response.FarmerProfileResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.payload.response.SubmissionsResponse;

import com.hua.myElga.service.BreederSubmissionService;
import com.hua.myElga.service.FarmerService;

import com.hua.myElga.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services/profile")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private BreederSubmissionService breederSubmissionService;

    @GetMapping("{email}")
    public ResponseEntity<?> getFarmerProfile(@PathVariable String email, Authentication auth) {
        // Check if user is authenticated
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Can not access this function - No authorization."));
        }

        // Take logged in user's id
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        // Call service to take farmer's profile
        FarmerProfileResponse response = farmerService.getFarmerProfile(email, loggedInUserId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> editFarmerProfile(@PathVariable Long id, @RequestBody FarmerProfileRequest request, Authentication auth) throws Exception {
        // Check if user is authenticated
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Can not access this function - No authorization."));
        }

        // Take logged in user's id
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        // Call service to update farmer's profile
        farmerService.updateFarmerProfile(request, id, loggedInUserId);

        return ResponseEntity.ok(new MessageResponse("Farmer's profile updated successfully!"));
    }

    @GetMapping("/{id}/my-submissions")
    public ResponseEntity<?> getMyApplications(@PathVariable Long id, Authentication auth) {
        // Check if user is authenticated
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Can not access this function - No authorization."));
        }

        // Take logged in user's id
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        // Call service to get breeder crop applications
        List<SubmissionsResponse> responseList = submissionService.getCropApplicationsOfUser(id, loggedInUserId);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}/my-breeders")
    public ResponseEntity<?> getMyBreeders(@PathVariable Long id, Authentication auth) {
        // Check if user is authenticated
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Can not access this function - No authorization."));
        }

        // Take logged in user's id
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        // Call service to get breeder applications
        List<BreedersResponse> responseList = breederSubmissionService.getBreederApplicationsOfUser(id, loggedInUserId);

        return ResponseEntity.ok(responseList);
    }
}
