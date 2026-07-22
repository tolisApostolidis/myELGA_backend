package com.hua.myElga.repository;

import com.hua.myElga.entity.BreederSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreederSubmissionRepository extends JpaRepository<BreederSubmission, Long> {
}
