package com.hua.myElga.repository;

import com.hua.myElga.entity.Damages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamagesRepository extends JpaRepository<Damages, Long> {
}
