package com.hua.myElga.repository;

import com.hua.myElga.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "submission")
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

}
