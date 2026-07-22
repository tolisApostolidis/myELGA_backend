package com.hua.myElga.controller;

import com.hua.myElga.config.JwtUtils;
import com.hua.myElga.payload.UserDetailsImpl;
import com.hua.myElga.payload.request.*;
import com.hua.myElga.payload.response.JwtUserResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.payload.response.UsersListResponse;
import com.hua.myElga.service.FarmerService;
import com.hua.myElga.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private FarmerService farmerService;

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if (roles.contains("ROLE_CITIZEN")) {
            // Send alert email to user's email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userDetails.getEmail());
            mailMessage.setSubject("Ειδοποίηση ασφαλείας");
            mailMessage.setText("Μόλις πραγματοποιήθηκε σύνδεση με το email σας στην πλατφόρμα myΕΛ.Γ.Α.");
            javaMailSender.send(mailMessage);
        }

        return ResponseEntity.ok(new JwtUserResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Call service to handle user's registration and matching to citizen role
        // It returns possible errors of registration or empty for ok response
        List<MessageResponse> errors = farmerService.registerUserAsFarmer(request);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(new MessageResponse("User signed up successful!"));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        // Call service in order to delete user with given email
        userService.deleteUserByEmail(email);

        return ResponseEntity.ok(new MessageResponse("User deleted!"));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        // Call service in order to take all users info
        List<UsersListResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }
}
