package com.hua.myElga.repository;


import com.hua.myElga.entity.ElgaManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<ElgaManager, Long> {
}
