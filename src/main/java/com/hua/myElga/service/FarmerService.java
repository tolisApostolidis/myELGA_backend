package com.hua.myElga.service;

import com.hua.myElga.entity.Farmer;
import com.hua.myElga.entity.User;
import com.hua.myElga.payload.request.FarmerProfileRequest;
import com.hua.myElga.payload.request.RegisterRequest;
import com.hua.myElga.payload.response.FarmerProfileResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.repository.FarmerRepository;
import com.hua.myElga.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    //// Register and link Farmer with given User ////
    @Transactional
    public List<MessageResponse> registerUserAsFarmer(RegisterRequest request) {
        // 1. Check if registration username and email are unique and return errors if so
        List<MessageResponse> errors = new ArrayList<>();

        if (userRepository.existsByUsername(request.getUsername())) {
            errors.add(new MessageResponse("Το όνομα χρήστη χρησιμοποιείτε ήδη"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            errors.add(new MessageResponse("Το email χρησιμοποιείτε ήδη"));
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        // 2. Create User object with given fields
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                bCryptPasswordEncoder.encode(request.getPassword())

        );

        // 3. Register user to DB
        User savedUser = userService.saveUser(user, "ROLE_CITIZEN");

        // 4. Create Farmer object and link saved user
        Farmer farmer = new Farmer(
                request.getFirstName(),
                request.getLastName(),
                request.getAfm(),
                request.getAddress(),
                request.getPhoneNumber()
        );
        farmer.setUser(savedUser);

        // 5. Register farmer to DB
        farmerRepository.save(farmer);

        return List.of();
    }

    //// Take farmer's profile ////
    @Transactional
    public FarmerProfileResponse getFarmerProfile(String email, Long loggedInUserId) {
        // 1. Find user by email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given email: " + email));

        // 2. Check if logged-in user matches path's credentials
        if (!user.getId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this profile");
        }

        // 3. Get farmer object
        Farmer farmer = user.getFarmer();

        if (farmer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer profile not found");
        }

        // 4. Build and send response
        return new FarmerProfileResponse(
                farmer.getFirstName(),
                farmer.getLastName(),
                farmer.getAddress(),
                farmer.getId(),
                farmer.getAFM(),
                farmer.getPhoneNumber()
        );
    }

    //// Update farmer's profile ////
    @Transactional
    public void updateFarmerProfile(FarmerProfileRequest request, Long id, Long loggedInUserId) {
        // 1. Find user by email
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + id));

        // 2. Check if logged-in user matches path's credentials
        if (!user.getId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this profile");
        }

        // 3. Check if user is linked to a farmer object
        Farmer farmer = user.getFarmer(); // Take farmer object of this user

        if (farmer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer profile not found");
        }

        // 4. Update profile with given values
        if (!request.getAddress().isEmpty()) {
            farmer.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null) {
            farmer.setPhoneNumber(request.getPhoneNumber());
        }

        // 5. Update DB
        farmerRepository.save(farmer);
    }
}
