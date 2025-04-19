package com.dusktildwan.spotifyservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.dusktildwan.common.DAL.entities")
@EnableJpaRepositories(basePackages = "com.dusktildwan.common.DAL.repositories")
public class JpaConfig {}
