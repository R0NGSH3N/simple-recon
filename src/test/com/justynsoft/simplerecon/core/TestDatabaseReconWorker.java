package com.justynsoft.simplerecon.core;

import com.justynsoft.simplerecon.core.worker.DatabaseReconWorker;
import com.justynsoft.simplerecon.core.worker.ReconWorker;
import com.justynsoft.simplerecon.core.worker.ReconWorkerEntity;
import com.justynsoft.simplerecon.core.worker.ReconWorkerStartEvent;
import com.justynsoft.simplerecon.traderecon.TradeAllocation;
import com.justynsoft.simplerecon.traderecon.TradeAllocationDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDatabaseReconWorker {

    @Autowired
    TradeAllocationDAO tradeAllocationDAO;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DatabaseReconWorker<TradeAllocation> databaseReconWorker;

    @Test
    public void testDefaultDatabaseWorker() {
        TradeAllocation allocation = new TradeAllocation();
        allocation.setBlockId("1234");
        allocation.setNetAmt(new BigDecimal(1.0));
        allocation.setPortfolioId("10W");
        allocation.setQuantity(new BigDecimal(1.0));
        tradeAllocationDAO.save(allocation);

        allocation = new TradeAllocation();
        allocation.setBlockId("2345");
        allocation.setNetAmt(new BigDecimal(2.0));
        allocation.setQuantity(new BigDecimal(1.0));
        tradeAllocationDAO.save(allocation);

        allocation = new TradeAllocation();
        allocation.setBlockId("1234");
        allocation.setNetAmt(new BigDecimal(3.0));
        allocation.setQuantity(new BigDecimal(1.0));
        tradeAllocationDAO.save(allocation);

        ReconWorkerEntity entity = new ReconWorkerEntity();
        entity.setId(1L);
        entity.setReconServiceId(1L);

        databaseReconWorker.setEntity(entity);
        List<TradeAllocation> result = databaseReconWorker.load(new ReconWorkerStartEvent(this));
        assertTrue(result.size() == 3);

        TradeAllocation firstAlloc = result.get(0);
        assertTrue(firstAlloc.getBlockId().equals("1234"));

        TradeAllocation secondAlloc = result.get(1);
        assertTrue(secondAlloc.getBlockId().equals("2345"));

        TradeAllocation thirdAlloc = result.get(2);
        assertTrue(secondAlloc.getBlockId().equals("1234"));

    }
}
