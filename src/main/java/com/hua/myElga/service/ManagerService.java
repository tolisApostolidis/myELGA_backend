package com.hua.myElga.service;

import com.hua.myElga.entity.ElgaManager;
import com.hua.myElga.entity.User;
import com.hua.myElga.payload.request.RegisterManagerRequest;
import com.hua.myElga.payload.response.ManagerResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.repository.ManagerRepository;
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
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public List<MessageResponse> registerNewManager(RegisterManagerRequest request) {
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
        User savedUser = userService.saveUser(user, "ROLE_MANAGER");

        // 4. Create Manager object and link saved user
        ElgaManager manager = new ElgaManager(
                request.getFirstName(),
                request.getLastName(),
                request.getAfm(),
                request.getJobAddress(),
                savedUser);

        // 5. Register manager to DB
        managerRepository.save(manager);

        return List.of();
    }

    @Transactional
    public ManagerResponse getManagerProfile(Long id, Long loggedInUserId) {
        // 1. Find user by email
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given id: " + id));

        // 2. Check if logged-in user matches path's credentials
        if (!user.getId().equals(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this profile");
        }

        // 3. Check if user is linked to a farmer object
        ElgaManager manager = user.getManager();

        if (manager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager profile not found");
        }

        // 4. Structure response
        ManagerResponse managerResponse = new ManagerResponse(
                manager.getId(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getAFM(),
                manager.getJobAddress()
        );

        return managerResponse;
    }
}
