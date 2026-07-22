package com.hua.myElga.repository;

import com.hua.myElga.entity.FileApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileApplicationRepository extends JpaRepository<FileApplication, Long> {
}
