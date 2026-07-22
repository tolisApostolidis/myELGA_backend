package com.hua.myElga.service;

import com.hua.myElga.entity.*;
import com.hua.myElga.payload.request.ManageSubmission;
import com.hua.myElga.payload.request.SubmissionRequest;
import com.hua.myElga.payload.response.SubmissionsResponse;
import com.hua.myElga.repository.SubmissionRepository;
import com.hua.myElga.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private DamagesService damagesService;

    //// HELPER METHOD ////
    private Farmer checkLinkOfUserToFarmer(Long id, Long loggedInUserId) {
        // Take user with given id
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + id));

        // Check if logged-in user matches path's credentials
        if (!user.getId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this profile");
        }

        // Check if user is linked to a farmer object
        Farmer farmer = user.getFarmer();

        if (farmer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer profile not found");
        }

        return farmer;
    }

    private Date takeFormattedCurrentDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        return formatter.parse(formatter.format(new Date()));
    }
    //// EOF ////

    //// Create crop application in citizen with given id ////
    @Transactional
    public void createCropApplicationOfUser(SubmissionRequest request, Long id, Long loggedInUserId) throws ParseException {
        // Check if request id matches logged-in user and if farmer object exists
        Farmer farmer = checkLinkOfUserToFarmer(id, loggedInUserId);

        // Create new submission and set fields
        Submission newSubmission = new Submission();
        newSubmission.setFieldArea(request.getFieldArea());
        newSubmission.setAverageProduction(request.getAverageProduction());
        newSubmission.setDescription(request.getDescription());
        newSubmission.setCreationDate(takeFormattedCurrentDate());

        // Link submission to farmer
        newSubmission.setFarmer(farmer);

        // Store it to DB
        submissionRepository.save(newSubmission);
    }

    //// Get crop application of given id ////
    @Transactional
    public SubmissionsResponse getCropApplicationOfUser(Long applicationId, Long loggedInUserId) {
        // 1. Find user by id
        User user = userRepository.findById(loggedInUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + loggedInUserId));

        // 2. Find application of given id
        Submission submission = submissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // 3. Check if user is admin or manager
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isManager = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_MANAGER"));

        // 4. Check ownership of application - give access if is admin or manager
        if (!isAdmin && !isManager) {
            // Take farmer object
            Farmer farmer = user.getFarmer();

            if (farmer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer profile not found");
            }

            if (!submission.getFarmer().getId().equals(farmer.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this application");
            }
        }

        // 5. Build response list
        Double damages;
        if(submission.getDamages() == null) {
            damages = (double) -1;
        }else {
            damages = submission.getDamages().getDamagesSum();
        }

        SubmissionsResponse response = new SubmissionsResponse(
                submission.getId(),
                submission.getFieldArea(),
                submission.getAverageProduction(),
                submission.getPricePerUnit(),
                submission.getDescription(),
                submission.getDamagePercentage(),
                submission.getState(),
                submission.getCreationDate(),
                damages
        );

        return response;
    }

    //// Get ALL crop applications of given User ////
    @Transactional
    public List<SubmissionsResponse> getCropApplicationsOfUser(Long id, Long loggedInUserId) {
        // 1. Check if the given user id matches the authenticated user and verify that a Farmer profile exists
        Farmer farmer = checkLinkOfUserToFarmer(id, loggedInUserId);

        // 2. Take all applications of farmer
        List<Submission> applications = farmer.getSubmissions();

        // 3. Iterate through list to format response list
        List<SubmissionsResponse> responseList = new ArrayList<>();
        for (Submission application : applications) {
            Double damages;

            if (application.getDamages() == null) {
                damages = (double) -1;
            } else {
                damages = application.getDamages().getDamagesSum();
            }

            responseList.add(new SubmissionsResponse(
                    application.getId(),
                    application.getFieldArea(),
                    application.getAverageProduction(),
                    application.getPricePerUnit(),
                    application.getDescription(),
                    application.getDamagePercentage(),
                    application.getState(),
                    application.getCreationDate(),
                    damages
            ));
        }

        return responseList;
    }

    //// Update citizen's crop application fields for given id ////
    @Transactional
    public void updateCropApplication(SubmissionRequest request, Long applicationId, Long id) {
        // 1. Find user by id
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + id));

        // 2. Check if user is linked to a farmer object
        Farmer farmer = user.getFarmer();

        if (farmer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer profile not found");
        }

        // 3. Find application of given id
        Submission application = submissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // 4. Check ownership of application
        if (!application.getFarmer().getId().equals(farmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this application");
        }

        // 5. Set given values to application fields
        if (request.getFieldArea() != 0) {
            application.setFieldArea(request.getFieldArea());
        }
        if (request.getAverageProduction() != 0) {
            application.setAverageProduction(request.getAverageProduction());
        }
        if (!request.getDescription().isEmpty()) {
            application.setDescription(request.getDescription());
        }

        // Store it to DB
        submissionRepository.save(application);
    }

    //// Delete citizen's crop application of given id ////
    @Transactional
    public void deleteSubmission(Long applicationId, Long id) {
        // 1. Take user with given id
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + id));

        // 2. Check if user is linked to a farmer object
        Farmer farmer = user.getFarmer();

        // 3. Find application of given id
        Submission application = submissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // 4. Check ownership of application
        if (!application.getFarmer().getId().equals(farmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this application");
        }

        // 4. Delete application from DB
        submissionRepository.deleteById(application.getId());
    }

    //// Get citizen's breeder application's PDF of given id ////
    @Transactional
    public ResponseEntity<byte[]> getBreederApplicationPDF(Long applicationId, Long userId) {
        // 1. Find application of given id
        Submission application = submissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // 2. Get file from DB from file service
        Long fileId =  application.getFileApplication().getId();
        byte[] fileData = fileService.getFile(fileId).getData();


        // 3. Structure response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("file.pdf", "filename.pdf");

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    //// Get ALL applications ////
    @Transactional
    public List<SubmissionsResponse> getAllSubmissions() {
        // 1. Get all applications from DB
        List<Submission> submissions = submissionRepository.findAll();

        // 2. Initialize response list
        List<SubmissionsResponse> responseList = new ArrayList<>();

        // 3. Iterate through list to format response list
        for (Submission submission : submissions) {
            Double damages;
            if(submission.getDamages() == null) {
                damages = (double) -1;
            }else {
                damages = submission.getDamages().getDamagesSum();
            }
            responseList.add(new SubmissionsResponse(
                    submission.getId(),
                    submission.getFieldArea(),
                    submission.getAverageProduction(),
                    submission.getPricePerUnit(),
                    submission.getDescription(),
                    submission.getDamagePercentage(),
                    submission.getState(),
                    submission.getCreationDate(),
                    damages));
        }

        return responseList;
    }

    //// Update state and generate PDF if needed ////
    @Transactional
    public void changeSubmissionsState(Long applicationId, ManageSubmission request) throws Exception {
        // Find application of given id
        Submission application = submissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // Set state of application
        // If is REJECTED continue
        // If is ACCEPTED generate PDF
        if ("REJECTED".equals(request.getState())) {
            application.setState("REJECTED");

            submissionRepository.save(application);
        } else if("ACCEPTED".equals(request.getState())) {
            //
            Double damages = application.calculateCompensation(request.getDamagePercentage(), request.getPricePerUnit());

            Damages savedDamages = damagesService.storeDamages(new Damages(damages));

            // Set application's fields with given values
            application.setState("ACCEPTED");
            application.setDamagePercentage(request.getDamagePercentage());
            application.setPricePerProductUnit(request.getPricePerUnit());
            application.setDamages(savedDamages);

            // Store it to DB
            submissionRepository.save(application);

            // Call service to generate PDF
            FileApplication storedFile = fileService.generateAndStorePdfFile(application);

            // Link File to application and store it to DB
            application.setFileApplication(storedFile);

            submissionRepository.save(application);
        }
    }
}
