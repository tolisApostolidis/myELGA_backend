package com.hua.myElga.config;

import com.hua.myElga.entity.*;
import com.hua.myElga.entity.ElgaManager;
import com.hua.myElga.entity.Farmer;
import com.hua.myElga.entity.User;
import com.hua.myElga.repository.RoleRepository;
import com.hua.myElga.service.InitializeAppData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InitializeDatabase {
    private static final List<String> ROLES = List.of("ROLE_CITIZEN", "ROLE_MANAGER", "ROLE_ADMIN");
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private InitializeAppData initializeAppData;

    //@PostConstruct
    public void setup() {
        //this.createAdmin();
        //this.createManagers();
        //this.createFarmer();
    }

    //// Create user with role ADMIN ////
    private void createAdmin() {
        // Create user object and set fields
        User user = new User();
        user.setUsername("Admin");
        user.setEmail("admin@example.gr");
        user.setPassword(bCryptPasswordEncoder.encode("ADMINPASS123!"));

        // Call service to store user with given role
        this.initializeAppData.saveAsUser(user, "ROLE_ADMIN");
    }

    //// Create user with role MANAGER ////
    private void createManagers() {
        // Create user object and set fields
        User user = new User();
        user.setUsername("exampleManager");
        user.setEmail("manager@example.gr");
        user.setPassword(bCryptPasswordEncoder.encode("pass"));

        // Call service to store user with given role
        User savedUser = this.initializeAppData.saveAsUser(user, "ROLE_MANAGER");

        // Create manager object and set fields
        ElgaManager manager = new ElgaManager("First name", "Last name", 123456789L, "City", savedUser);

        // Call service to store manager with given fields
        this.initializeAppData.saveAsManager(manager, savedUser.getId());
    }

    //// Create user with role FARMER ////
    private void createFarmer() {
        // Create user object and set fields
        User user = new User();
        user.setUsername("exampleFarmer");
        user.setEmail("farmer@example.com");
        user.setPassword(bCryptPasswordEncoder.encode("pass"));

        // Call service to store user with given role
        User savedUser = this.initializeAppData.saveAsUser(user, "ROLE_CITIZEN");

        // Create farmer object and set fields
        Farmer farmer = new Farmer("First name", "Last name", 123456789L, "City", 6983450273L, savedUser);

        // Call service to store user with given role
        this.initializeAppData.saveAsFarmer(farmer, savedUser.getId());
    }
}
