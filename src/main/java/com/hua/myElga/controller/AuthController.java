package com.hua.myElga.controller;

import com.hua.myElga.config.JwtUtils;
import com.hua.myElga.payload.UserDetailsImpl;
import com.hua.myElga.payload.request.*;
import com.hua.myElga.payload.response.JwtUserResponse;
import com.hua.myElga.payload.response.MessageResponse;
import com.hua.myElga.payload.response.UsersListResponse;
import com.hua.myElga.service.EmailService;
import com.hua.myElga.service.FarmerService;
import com.hua.myElga.service.LoginInfoService;
import com.hua.myElga.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private EmailService emailService;
    @Autowired
    private FarmerService farmerService;
    @Autowired
    private LoginInfoService loginInfoService;

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) throws MessagingException, IOException {
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
            String userAgent = request.getHeader("User-Agent");
            String date = loginInfoService.getLoginDate();
            String ip = loginInfoService.getClientIp(request);
            String browser = loginInfoService.getBrowser(userAgent);
            String platform = loginInfoService.getPlatform(userAgent);
            String location = loginInfoService.getLocation(ip);

            emailService.sendLoginNotification(
                    userDetails.getEmail(),
                    userDetails.getUsername(),
                    date,
                    location,
                    ip,
                    browser,
                    platform
            );
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
