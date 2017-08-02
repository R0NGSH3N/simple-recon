package com.justynsoft.simplerecon.core.springconfig;

import com.justynsoft.simplerecon.core.service.ReconServiceLoader;
import com.justynsoft.simplerecon.core.worker.ReconWorkersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SimpleReconciliationConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public ReconServiceLoader getReconServiceLoader(){
        return new ReconServiceLoader();
    }

    @Bean
    public ReconWorkersManager getReconWorkerStartEventListener(){
        return new ReconWorkersManager();
    }
}
