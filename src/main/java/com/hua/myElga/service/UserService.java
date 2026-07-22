package com.hua.myElga.service;

import com.hua.myElga.entity.Role;
import com.hua.myElga.entity.User;
import com.hua.myElga.payload.UserDetailsImpl;
import com.hua.myElga.payload.response.UsersListResponse;
import com.hua.myElga.repository.RoleRepository;
import com.hua.myElga.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    //// Register given User ////
    public User saveUser(User user, String roleToAdd) {
        // Find given role if exists
        Role role = roleRepository.findByName(roleToAdd).orElseThrow(() -> new IllegalStateException("Required role is false: " + roleToAdd));

        // Add to user the list of given roles
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Register user
        return userRepository.save(user);
    }

    //// Delete user with given email ////
    @Transactional
    public void deleteUserByEmail(String email) {
        // Try to find user with given email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with given email: " + email));

        // Delete user
        userRepository.delete(user);
    }

    //// Get ALL Users ////
    @Transactional
    public List<UsersListResponse> getAllUsers() {
        // Get all users
        List<User> users = userRepository.findAll();

        // Initialize list for response
        List<UsersListResponse> responseList = new ArrayList<>();

        // For every user get details
        for (User user : users) {
            // Get roles
            Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

            // Get user's details and add to list
            responseList.add(new UsersListResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    roles
            ));
        }

        return responseList;
    }
}