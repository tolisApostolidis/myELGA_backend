package com.hua.myElga.service;

import com.hua.myElga.entity.Damages;
import com.hua.myElga.repository.DamagesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DamagesService {

    @Autowired
    private DamagesRepository damagesRepository;

    //// Register Damages to DB ////
    @Transactional
    public Damages storeDamages(Damages damages) {
        return damagesRepository.save(damages);
    }
}
