package com.hua.myElga.repository;

import com.hua.myElga.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource(path = "farmer")
public interface FarmerRepository extends JpaRepository<Farmer, Long>{
}
