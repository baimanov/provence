package com.example.provence.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseConfig {

//    @Bean
//    public DataSource dataSource() {
//        return DataSourceBuilder
//                .create()
//                .url("jdbc:postgresql://dpg-ck9a2jegtj9c73ba1ti0-a.singapore-postgres.render.com/provence")
//                .username("postgres1")
//                .password("vUXhNhUjMaeRegFUx8TcttWD6mVP8aHi")
//                .build();
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//    @Bean
//    public ResourceDatabasePopulator resourceDatabasePopulator() {
//        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//
//        // List of SQL script file names
//        List<String> scriptFiles = Arrays.asList("category.sql", "data.sql");
//
//        for (String script : scriptFiles) {
//            populator.addScript(new ClassPathResource(script));
//        }
//
//        return populator;
//    }
}
