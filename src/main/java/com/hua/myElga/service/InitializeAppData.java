package com.hua.myElga.service;

import com.hua.myElga.repository.*;
import com.hua.myElga.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InitializeAppData {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private FarmerRepository farmerRepository;

    @Transactional
    public User saveAsUser(User user, String roleToAdd) {
        Role role = roleRepository.findByName(roleToAdd).orElseThrow(() -> new IllegalStateException("Could not match given role to system roles: " + roleToAdd));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        return this.userRepository.save(user);
    }

    @Transactional
    public void saveAsManager(ElgaManager manager, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find user iwth id: " + id));
        manager.setUser(user);
        managerRepository.save(manager);
    }
    @Transactional
    public Farmer saveAsFarmer(Farmer farmer, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Could not find user iwth id: " + id));
        farmer.setUser(user);
        return farmerRepository.save(farmer);
    }
}
