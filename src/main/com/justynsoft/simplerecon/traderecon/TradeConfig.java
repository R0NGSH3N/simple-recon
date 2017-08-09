package com.justynsoft.simplerecon.traderecon;

import com.justynsoft.simplerecon.core.worker.CSVFileReconWorker;
import com.justynsoft.simplerecon.core.worker.DatabaseReconWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class TradeConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public DatabaseReconWorker TradeAllocationDatabaseReconWorker(){
        DatabaseReconWorker databaseWorker = new DatabaseReconWorker();
        databaseWorker.setSQL("SELECT * FROM allocation");
        databaseWorker.setJdbcTemplate(this.jdbcTemplate);
        databaseWorker.setClazz(TradeAllocation.class);
        return databaseWorker;
    }

    @Bean
    public CSVFileReconWorker TradeAllocationCSVReconWorker(){
        CSVFileReconWorker csvFileReconWorker = new CSVFileReconWorker();
        csvFileReconWorker.setClazz(TradeAllocation.class);
        csvFileReconWorker.setFileName("/com/justynsoft/simplerecon/traderecon/data.csv");
        return csvFileReconWorker;
    }
}
