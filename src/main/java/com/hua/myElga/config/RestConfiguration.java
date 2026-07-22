package com.hua.myElga.config;

import com.hua.myElga.entity.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer  {

    //// Exposes entity IDs in REST responses ////
    @Override
    public void configureRepositoryRestConfiguration (RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Submission.class);
        config.exposeIdsFor(BreederSubmission.class);
        config.exposeIdsFor(FileApplication.class);
        config.exposeIdsFor(Damages.class);
        config.exposeIdsFor(Farmer.class);
    }
}
