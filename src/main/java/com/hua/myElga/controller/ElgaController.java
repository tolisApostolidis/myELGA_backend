package com.hua.myElga.controller;

import com.hua.myElga.payload.UserDetailsImpl;
import com.hua.myElga.payload.request.ManageBreeder;
import com.hua.myElga.payload.request.ManageSubmission;
import com.hua.myElga.payload.request.RegisterManagerRequest;
import com.hua.myElga.payload.response.BreedersResponse;
import com.hua.myElga.payload.response.ManagerResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.payload.response.SubmissionsResponse;
import com.hua.myElga.service.BreederSubmissionService;
import com.hua.myElga.service.ManagerService;
import com.hua.myElga.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services/manager")
public class ElgaController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private BreederSubmissionService breederSubmissionService;

    //// HELPER METHODS ////
    private boolean isManagerOrAdmin(Authentication auth) {
        // Check if auth is valid
        if (auth == null) {
            return false;
        }

        // Check if auth is manager or admin
        return auth.getAuthorities().stream().anyMatch(a -> "ROLE_MANAGER".equals(a.getAuthority()) || "ROLE_ADMIN".equals(a.getAuthority()));
    }
    //// EOF ////

    //// GET PROFILE OF MANAGER WITH GIVEN ID ////
    @GetMapping("/{id}")
    public ResponseEntity<?> getManagerProfile(@PathVariable Long id, Authentication auth) {
        // Take logged in user's id
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        // Call service to get manager's profile
        ManagerResponse managerResponse = managerService.getManagerProfile(id, loggedInUserId);

        return ResponseEntity.ok(managerResponse);
    }
    //// EOF ////

    //// GET ALL APPLICATION OF TYPE ////
    @GetMapping("/submissions")
    public ResponseEntity<?> getAllCropApplications(Authentication auth) {
        // Check if logged-in user has manager or admin role attached to it
        boolean hasAccess = isManagerOrAdmin(auth);

        // If not has access return forbidden status
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Can not access this function - Access denied."));
        }

        // Call service to get all crop applications
        List<SubmissionsResponse> responseList = submissionService.getAllSubmissions();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/breeders")
    public ResponseEntity<?> getAllBreederApplications(Authentication auth) {
        // Check if logged-in user has manager or admin role attached to it
        boolean hasAccess = isManagerOrAdmin(auth);

        // If not has access return forbidden status
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Can not access this function - Access denied."));
        }

        // Call service to get all crop applications
        List<BreedersResponse> responseList = breederSubmissionService.getAllSubmissions();

        return ResponseEntity.ok(responseList);
    }
    //// EOF ////

    //// UPDATE APPLICATION STATE OF GIVEN TYPE ////
    @PostMapping("/{applicationId}/breeder")
    public ResponseEntity<?> updateBreeder(@PathVariable Long applicationId, @RequestBody ManageBreeder request, Authentication auth) throws Exception {
        // Check if logged-in user has manager or admin role attached to it
        boolean hasAccess = isManagerOrAdmin(auth);

        // If not has access return forbidden status
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Can not access this function - Access denied."));
        }

        // Call service to update application's state and generate PDF if needed
        breederSubmissionService.changeSubmissionsState(applicationId, request);

        return ResponseEntity.ok(new MessageResponse("Application updated successfully!"));
    }

    @PostMapping("/{applicationId}/submission")
    public ResponseEntity<?> updateSubmission(@PathVariable Long applicationId, @RequestBody ManageSubmission request, Authentication auth) throws Exception {
        // Check if logged-in user has manager or admin role attached to it
        boolean hasAccess = isManagerOrAdmin(auth);

        // If not has access return forbidden status
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Can not access this function - Access denied."));
        }

        // Call service to update application's state and generate PDF if needed
        submissionService.changeSubmissionsState(applicationId, request);

        return ResponseEntity.ok(new MessageResponse("Application updated successfully!"));
    }
    //// EOF ////

    //// CREATE USER AS MANAGER - ADMIN ONLY ////
    @Secured("ROLE_ADMIN")
    @PostMapping("/new")
    public ResponseEntity<?> createManager(@Valid @RequestBody RegisterManagerRequest request) {
        // Call service to create User as Manager
        List<MessageResponse> errors = managerService.registerNewManager(request);

        // If errors send bad request
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(new MessageResponse("User signed up successfully!"));
    }
}
