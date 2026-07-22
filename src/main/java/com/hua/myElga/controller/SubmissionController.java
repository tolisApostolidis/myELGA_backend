package com.hua.myElga.controller;

import com.hua.myElga.entity.User;
import com.hua.myElga.payload.UserDetailsImpl;
import com.hua.myElga.payload.request.BreederRequest;
import com.hua.myElga.payload.request.SubmissionRequest;
import com.hua.myElga.payload.response.BreedersResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.payload.response.SubmissionsResponse;
import com.hua.myElga.repository.UserRepository;
import com.hua.myElga.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/services")
public class SubmissionController {
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private BreederSubmissionService breederSubmissionService;
    @Autowired
    private UserRepository userRepository;

    //// HELPER METHODS ////
    private User checkUserAuthorization(Authentication auth) {
        // Check if user is authenticated
        if (auth == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        // Take logged in user's id
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        return userRepository.findById(loggedInUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    //// EOF ////

    //// GET APPLICATION OF TYPE ////
    @GetMapping("/farmer/{applicationId}")
    public ResponseEntity<?> getSubmission(@PathVariable Long applicationId, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service to get info of given application's id
        SubmissionsResponse response = submissionService.getCropApplicationOfUser(applicationId, currentUser.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/breeder/{applicationId}")
    public ResponseEntity<?> getBreeder(@PathVariable Long applicationId, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service to get info of given application's id
        BreedersResponse response = breederSubmissionService.getBreederApplicationOfUser(applicationId, currentUser.getId());

        return ResponseEntity.ok(response);
    }
    //// EOF ////

    //// UPDATE APPLICATION OF TYPE ////
    @PostMapping("/farmer/{applicationId}")
    public ResponseEntity<?> updateCropApplication(@PathVariable Long applicationId, @RequestBody SubmissionRequest request, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service in order to update application
        submissionService.updateCropApplication(request, applicationId, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Application was updated successfully!"));
    }

    @PostMapping("/breeder/{applicationId}")
    public ResponseEntity<?> updateBreederApplication(@PathVariable Long applicationId, @RequestBody BreederRequest request, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service in order to update application
        breederSubmissionService.updateBreederApplication(request, applicationId, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Application was updated successfully!"));
    }
    //// EOF ////

    //// DELETE APPLICATION OF TYPE ////
    @DeleteMapping("/farmer/{applicationId}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long applicationId, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service in order to delete application
        submissionService.deleteSubmission(applicationId, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Application deleted successfully!"));
    }

    @DeleteMapping("/breeder/{applicationId}")
    public ResponseEntity<?> deleteBreederSubmission(@PathVariable Long applicationId, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service in order to delete application
        breederSubmissionService.deleteApplication(applicationId, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Application deleted successfully!"));
    }
    //// EOF ////

    //// CREATE APPLICATION OF TYPE ////
    @PostMapping("/farmer/new/{id}")
    public ResponseEntity<?> createCropApplication(@PathVariable Long id, @RequestBody SubmissionRequest request, Authentication auth) throws Exception {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service to create new submission
        submissionService.createCropApplicationOfUser(request, id, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Created application successfully!"));
    }

    @PostMapping("/breeder/new/{id}")
    public ResponseEntity<?> createBreederApplication(@PathVariable Long id, @RequestBody BreederRequest request, Authentication auth) throws Exception {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service to create new submission
        breederSubmissionService.createBreederApplicationOfUser(request, id, currentUser.getId());

        return ResponseEntity.ok(new MessageResponse("Created application successfully!"));
    }
    //// EOF ////

    //// GET APPLICATION OF TYPE PDF ////
    @GetMapping("/farmer/{applicationId}/download")
    public ResponseEntity<?> getPdfOfSubmission(@PathVariable Long applicationId, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service to get crop application's PDF
        ResponseEntity<byte[]> response = submissionService.getBreederApplicationPDF(applicationId, currentUser.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/breeder/{applicationId}/download")
    public ResponseEntity<?> getPdfOfBreeder(@PathVariable Long applicationId, Authentication auth) {
        // Check authentication
        User currentUser = checkUserAuthorization(auth);

        // Call service to get crop application's PDF
        ResponseEntity<byte[]> response = breederSubmissionService.getBreederApplicationPDF(applicationId, currentUser.getId());

        return ResponseEntity.ok(response);
    }
    //// EOF ////
}
