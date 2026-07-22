package com.hua.myElga.service;

import com.hua.myElga.entity.*;
import com.hua.myElga.payload.request.BreederRequest;
import com.hua.myElga.payload.request.ManageBreeder;
import com.hua.myElga.payload.response.BreedersResponse;
import com.hua.myElga.repository.BreederSubmissionRepository;
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
public class BreederSubmissionService {

    @Autowired
    private BreederSubmissionRepository breederSubmissionRepository;
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

    //// Create breeder application in citizen with given id ////
    @Transactional
    public void createBreederApplicationOfUser(BreederRequest request, Long id, Long loggedInUserId) throws ParseException {
        // Check if request id matches logged-in user and if farmer object exists
        Farmer farmer = checkLinkOfUserToFarmer(id, loggedInUserId);

        // Create new submission and set fields
        BreederSubmission newApplication = new BreederSubmission();
        newApplication.setNumberOfKilledAnimals(request.getKilledAnimals());
        newApplication.setTotalAnimals(request.getTotalAnimals());
        newApplication.setPricePerUnit(request.getPricePerUnit());
        newApplication.setCreationDate(takeFormattedCurrentDate());

        // Link submission to farmer
        newApplication.setFarmer(farmer);

        // Store it to DB
        breederSubmissionRepository.save(newApplication);
    }

    //// Get breeder application of given id ////
    @Transactional
    public BreedersResponse getBreederApplicationOfUser(Long applicationId, Long loggedInUserId) {
        // 1. Find user by id
        User user = userRepository.findById(loggedInUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + loggedInUserId));

        // 2. Find application of given id
        BreederSubmission application = breederSubmissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

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

            if (!application.getFarmer().getId().equals(farmer.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this application");
            }
        }

        // 5. Build response list
        Double damages;
        if(application.getDamages() == null) {
            damages = (double) -1;
        }else {
            damages = application.getDamages().getDamagesSum();
        }

        BreedersResponse response = new BreedersResponse(
                application.getId(),
                application.getCreationDate(),
                application.getTotalAnimals(),
                application.getState(),
                application.getResidualValue(),
                application.getPricePerUnit(),
                application.getNumberOfKilledAnimals(),
                application.getDamageCoveragePercentage(),
                application.getCompensationFactor(),
                damages
        );

        return response;
    }

    //// Get ALL breeder applications of given User ////
    @Transactional
    public List<BreedersResponse> getBreederApplicationsOfUser(Long id, Long loggedInUserId) {
        // 1. Check if the given user id matches the authenticated user and verify that a Farmer profile exists
        Farmer farmer = checkLinkOfUserToFarmer(id, loggedInUserId);

        // 2. Take all applications of farmer
        List<BreederSubmission> applications = farmer.getBreederSubmissions();

        // 3. Iterate through list to format response list
        List<BreedersResponse> responseList = new ArrayList<>();
        for (BreederSubmission application : applications) {
            Double damages;

            if (application.getDamages() == null) {
                damages = (double) -1;
            } else {
                damages = application.getDamages().getDamagesSum();
            }

            responseList.add(new BreedersResponse(
                    application.getId(),
                    application.getCreationDate(),
                    application.getTotalAnimals(),
                    application.getState(),
                    application.getResidualValue(),
                    application.getPricePerUnit(),
                    application.getNumberOfKilledAnimals(),
                    application.getDamageCoveragePercentage(),
                    application.getCompensationFactor(),
                    damages
            ));
        }

        return responseList;
    }

    //// Update citizen's breeder application fields for given id ////
    @Transactional
    public void updateBreederApplication(BreederRequest request, Long applicationId, Long id) {
        // 1. Find user by id
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + id));

        // 2. Check if user is linked to a farmer object
        Farmer farmer = user.getFarmer();

        if (farmer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer profile not found");
        }

        // 3. Find application of given id
        BreederSubmission application = breederSubmissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // 4. Check ownership of application
        if (!application.getFarmer().getId().equals(farmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this application");
        }

        // 5. Set given values to application fields
        if (request.getKilledAnimals() != 0) {
            application.setNumberOfKilledAnimals(request.getKilledAnimals());
        }
        if (request.getTotalAnimals() != 0) {
            application.setTotalAnimals(request.getTotalAnimals());
        }
        if (request.getPricePerUnit() != null) {
            application.setPricePerUnit(request.getPricePerUnit());
        }

        // Store it to DB
        breederSubmissionRepository.save(application);
    }

    //// Delete citizen's breeder application of given id ////
    @Transactional
    public void deleteApplication(Long applicationId, Long userId) {
        // 1. Take user with given id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + userId));

        // 2. Check if user is linked to a farmer object
        Farmer farmer = user.getFarmer();

        // 3. Find application of given id
        BreederSubmission application = breederSubmissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // 4. Check ownership of application
        if (!application.getFarmer().getId().equals(farmer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this application");
        }

        // 4. Delete application from DB
        breederSubmissionRepository.deleteById(application.getId());
    }

    //// Get citizen's crop application's PDF of given id ////
    @Transactional
    public ResponseEntity<byte[]> getBreederApplicationPDF(Long applicationId, Long userId) {
        // 1. Find application of given id
        BreederSubmission application = breederSubmissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

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
    public List<BreedersResponse> getAllSubmissions() {
        // 1. Get all applications from DB
        List<BreederSubmission> applications = breederSubmissionRepository.findAll();

        // 2. Initialize response list
        List<BreedersResponse> responseList = new ArrayList<>();

        // 3. Iterate through list to format response list
        for (BreederSubmission b : applications) {
            Double damages;
            if ( b.getDamages() == null ) {
                damages = (double) -1;
            } else {
                damages = b.getDamages().getDamagesSum();
            }
            responseList.add(new BreedersResponse(
                    b.getId(),
                    b.getCreationDate(),
                    b.getTotalAnimals(),
                    b.getState(),
                    b.getResidualValue(),
                    b.getPricePerUnit(),
                    b.getNumberOfKilledAnimals(),
                    b.getDamageCoveragePercentage(),
                    b.getCompensationFactor(),
                    damages));
        }

        return responseList;
    }

    //// Update state and generate PDF if needed ////
    @Transactional
    public void changeSubmissionsState(Long applicationId, ManageBreeder request) throws Exception {
        // Find application of given id
        BreederSubmission application = breederSubmissionRepository.findById(applicationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + applicationId));

        // Set state of application
        // If is REJECTED continue
        // If is ACCEPTED generate PDF
        if ("REJECTED".equals(request.getState())) {
            application.setState("REJECTED");

            breederSubmissionRepository.save(application);
        } else if("ACCEPTED".equals(request.getState())) {
            //
            Double damages = application.calculateCompensation(request.getDamagePercentage(), request.getCompensationFactor(), request.getResidualValue());

            Damages savedDamages = damagesService.storeDamages(new Damages(damages));

            // Set application's fields with given values
            application.setState("ACCEPTED");
            application.setDamageCoveragePercentage(request.getDamagePercentage());
            application.setCompensationFactor(request.getCompensationFactor());
            application.setResidualValue(request.getResidualValue());
            application.setDamages(savedDamages);

            // Store it to DB
            breederSubmissionRepository.save(application);

            // Call service to generate PDF
            FileApplication storedFile = fileService.generateAndStorePdfFile(application);

            // Link File to application and store it to DB
            application.setFileApplication(storedFile);

            breederSubmissionRepository.save(application);
        }
    }
}
